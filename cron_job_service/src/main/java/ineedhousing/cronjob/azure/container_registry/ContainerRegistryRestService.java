package ineedhousing.cronjob.azure.container_registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.azure.container_registry.model.DigestDto;
import ineedhousing.cronjob.azure.container_registry.model.ManifestsDeletedDto;
import ineedhousing.cronjob.azure.container_registry.model.TagsDto;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import io.quarkus.logging.Log;


@ApplicationScoped
public class ContainerRegistryRestService {

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

    private String containerUsername;
    private String containerAccessKey;

    @PostConstruct //only set the values during runtime to allow for native image
    void init() {
         containerUsername = config.getOptionalValue("azure.container.username", String.class)
                .orElseThrow(() -> new RuntimeException("Container Username not present!"));
         containerAccessKey =  config.getOptionalValue("azure.container.access.key", String.class)
                .orElseThrow(() -> new RuntimeException("Container Access Key not present!"));
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
                        Log.info("Successfully fetched digest for tag: " + tag);
                        return digest;
                    }
                    catch (Exception e) {
                        Log.error("Failed to fetch manifest for tag: " + tag);
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
                                Log.info("Successfully fetched digest for tag: " + tag);
                                return digest;
                            } catch (Exception e) {
                                Log.error("Failed to fetch manifest for tag: " + tag + " - " + e.getMessage());
                                return null;
                            }
                        }, virtualThreadExecutor)
                        .thenCompose(digest -> {
                            // Skip deletion if fetch failed or returned empty digest
                            if (digest == null || digest.digestDto().schemaVersion().equals(-1)) {
                                Log.warn("Skipping deletion for invalid digest from tag: " + tag);
                                return CompletableFuture.completedFuture(null);
                            }

                            return CompletableFuture.runAsync(() -> {
                                String digestValue = DigestDto.getDigestValue(digest);
                                try {
                                    deleteManifest(repository, digestValue);
                                    Log.info("Successfully deleted manifest: " + digestValue);
                                    manifestsDeleted.add(digestValue);
                                } catch (Exception e) {
                                    Log.error("Failed to delete digest: " + digestValue + " - " + e.getMessage());
                                    manifestsFailedToDelete.add(digestValue);
                                }
                            }, virtualThreadExecutor);
                        })
                        // Handle fetch failures gracefully
                        .exceptionally(throwable -> {
                            Log.error("Fetch-delete pipeline failed for tag: " + tag + " - " + throwable.getMessage());
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
