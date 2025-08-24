package ineedhousing.cronjob.azure.container_registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.azure.container_registry.models.DigestDto;
import ineedhousing.cronjob.azure.container_registry.models.ManifestsDeletedDto;
import ineedhousing.cronjob.azure.container_registry.models.TagsDto;
import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import io.quarkus.logging.Log;


@ApplicationScoped
public class ContainerRegistryService {

    @Inject
    @RestClient
    ContainerRegisterRestClient restClient;

    @Inject
    @VirtualThreads
    ExecutorService virtualThreadExecutor;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Config config;

    @Inject
    LogService logService;

    private String containerUsername;
    private String containerAccessKey;

    @PostConstruct //only set the values during runtime to allow for native image
    void init() {
         containerUsername = config.getOptionalValue("azure.container.username", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Container Username not present!"));
         containerAccessKey =  config.getOptionalValue("azure.container.access.key", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Container Access Key not present!"));
    }

    private final String ACCEPT_MEDIA = "application/vnd.docker.distribution.manifest.v2+json";

    public String getRepositories() {
       return restClient.listRepositories(getAuthHeader());
    }

    /**
     * Fetchs all tags of a repo
     * @param repository
     * @return
     * @throws JsonProcessingException
     */
    public TagsDto listTags(String repository) throws JsonProcessingException {
        String tags =  restClient.listTags(repository, getAuthHeader());
        return objectMapper.readValue(tags, TagsDto.class);
    }

    /**
     * Returns the Manifest of a tag
     * @param repository
     * @param tag
     * @return
     * @throws JsonProcessingException
     */
    public DigestDto.CompleteManifestInfoDto getManifestByTag(String repository, String tag) throws JsonProcessingException {
        Response response =  restClient.getManifestWithHeaders(repository, tag, getAuthHeader(), ACCEPT_MEDIA);
        String manifest = response.getHeaderString("Docker-Content-Digest");
        if (manifest == null) {
            throw new NotFoundException("Manifest not found");
        }
        DigestDto digestResponse = objectMapper.readValue(response.readEntity(String.class), DigestDto.class);
        return DigestDto.getCompleteManifestDto(digestResponse, tag, manifest);
    }

    /**
     * Fetchs all manifests of a given a list of tags at the same time via virtual threads
     * @param repository
     * @param tags
     * @return
     * @throws JsonProcessingException
     */
    public List<DigestDto.CompleteManifestInfoDto> getManifestsByTags(String repository, List<String> tags) {
        List<CompletableFuture<DigestDto.CompleteManifestInfoDto>> digestDtoFutures = tags.stream()
                .map(tag -> CompletableFuture.supplyAsync(() -> {
                    try {
                        DigestDto.CompleteManifestInfoDto digest = getManifestByTag(repository, tag);
                        logService.publish("Successfully fetched manifest for tag: " + tag, LoggingLevel.INFO);
                        return digest;
                    }
                    catch (Exception e) {
                        logService.publish("Failed to fetch manifest for tag: " + tag, LoggingLevel.ERROR);
                        return null;
                    }
                }, virtualThreadExecutor)).toList();

        CompletableFuture.allOf(digestDtoFutures.toArray(new CompletableFuture[0]))
                .join();

        List<DigestDto.CompleteManifestInfoDto> digestDtos = digestDtoFutures.stream()
                .map(future -> {
                    try {
                        DigestDto.CompleteManifestInfoDto digest = future.get();
                        return Objects.requireNonNullElseGet(digest, DigestDto::createEmptyDigest);
                    }
                    catch (Exception e) {
                        return DigestDto.createEmptyDigest();
                    }
                }).toList();

        return digestDtos;
    }

    /**
     * Deletes an Image based off its manifest
     * @param repository
     * @param digest
     */
    public void deleteManifest(String repository, String digest) {
        restClient.deleteManifest(repository, digest, getAuthHeader());
    }

    /**
     * Bulk deletes old images while always leaving 3 images
     * @param repository
     * @throws JsonProcessingException
     */
    public ManifestsDeletedDto deleteOldImages(String repository) throws JsonProcessingException {
        //fetch all tags
        TagsDto tags = listTags(repository);
        List<String> tagsList = tags.tags();

        if (tagsList.size() <= 4) { //always have 4 images (3 tags + "latest" tag)
            return new ManifestsDeletedDto(new ArrayList<>(), new ArrayList<>());
        }

        // leave the 3 newest tags ie images of INeedHousing backend while deleting the older ones
        List<String> tagsToBeDeleted = tagsList.stream()
                .filter(tag -> !tag.equals("latest"))
                .map(tag -> tag.substring(1))
                .sorted(Comparator.comparing(Long::valueOf))
                .limit(Math.max(0, tagsList.size() - 3))
                .map(epoc -> "v"+epoc)
                .toList();

        List<String> manifestsDeleted = Collections.synchronizedList(new ArrayList<>());
        List<String> manifestsFailedToDelete = Collections.synchronizedList(new ArrayList<>());;

        List<CompletableFuture<Void>> fetchAndDeleteFutures = tagsToBeDeleted.stream()
                .map(tag -> CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                DigestDto.CompleteManifestInfoDto digest = getManifestByTag(repository, tag);
                                logService.publish("Successfully fetched manifest for tag: " + tag, LoggingLevel.INFO);
                                return digest;
                            } catch (Exception e) {
                                logService.publish("Failed to fetch manifest for tag: " + tag, LoggingLevel.ERROR);
                                return null;
                            }
                        }, virtualThreadExecutor)
                        .thenCompose(digest -> {
                            // Skip deletion if fetch failed or returned empty digest
                            if (digest == null || digest.digestDto().schemaVersion().equals(-1)) {
                                logService.publish("Skipping deletion for invalid digest from tag: " + tag, LoggingLevel.WARN);
                                return CompletableFuture.completedFuture(null);
                            }

                            return CompletableFuture.runAsync(() -> {
                                String digestValue = DigestDto.getDigestValue(digest);
                                try {
                                    deleteManifest(repository, digestValue);
                                    Log.info("Successfully deleted manifest: " + digestValue);
                                    logService.publish("Successfully deleted manifest: " + digestValue, LoggingLevel.INFO);
                                    manifestsDeleted.add(digestValue);
                                } catch (Exception e) {
                                    logService.publish("Failed to delete digest: " + digestValue + " - " + e.getMessage(), LoggingLevel.ERROR);
                                    manifestsFailedToDelete.add(digestValue);
                                }
                            }, virtualThreadExecutor);
                        })
                        // Handle fetch failures gracefully
                        .exceptionally(throwable -> {
                            logService.publish("Fetch-delete pipeline failed for tag: " + tag + " - " + throwable.getMessage(), LoggingLevel.ERROR);
                            return null;
                        }))
                .toList();

        // Wait for all operations to complete
        CompletableFuture.allOf(fetchAndDeleteFutures.toArray(new CompletableFuture[0]))
                .join();

        return new ManifestsDeletedDto(manifestsDeleted, manifestsFailedToDelete);
    }

    private String getAuthHeader() {
        return ContainerRegistryAuthHeaderUtil.createBasicAuthHeader(containerUsername, containerAccessKey);
    }

}
