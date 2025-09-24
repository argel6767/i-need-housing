package ineedhousing.cronjob.gcp.artifact_registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.gcp.GCPServiceAccessKeyDecoder;
import ineedhousing.cronjob.gcp.models.ArtifactRegistryPackage;
import ineedhousing.cronjob.gcp.models.GetImagesDto;
import ineedhousing.cronjob.gcp.models.ImageVersionDto;
import ineedhousing.cronjob.gcp.models.ImageVersionDto.*;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ArtifactRegistryService {

    @Inject
    @RestClient
    ArtifactRegistryRestClient artifactRegistryRestClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    LogService logService;

    @Inject
    GCPServiceAccessKeyDecoder gcpServiceAccessKeyDecoder;

    public GetImagesDto getImages(String project, String location, String repository) throws IOException {
        logService.publish("Fetching images for " + repository, LoggingLevel.INFO);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String bearerHeader = getServiceAccessKeyHeader();
        String response =  artifactRegistryRestClient.listImages(project, location, repository, bearerHeader);
        GetImagesDto responseObject =  objectMapper.readValue(response, GetImagesDto.class);
        stopWatch.stop();
        logService.publish(String.format("%d image names fetched for %s. Runtime %s", responseObject.packages().size(), repository, stopWatch.getTime()), LoggingLevel.INFO);
        return responseObject;
    }

    public void deleteOldImages(String project, String location, String repository) throws IOException {
        logService.publish("Deleting stale images for " + repository, LoggingLevel.INFO);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String bearerHeader = getServiceAccessKeyHeader();

        GetImagesDto response = getImages(project, location, repository);
        List<ArtifactRegistryPackage> packages = response.packages();

        if (packages.size() < 4) {
            stopWatch.stop();
            logService.publish("Minimum amount of images present, skipping deletion. Runtime: " + stopWatch.getTime(), LoggingLevel.INFO);
            return;
        }

        List<String> imageNames = packages.stream()
                .map(this::getImageTimeTag)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Long::valueOf))
                .limit(Math.max(0, packages.size() - 3))
                .map(tag -> "image-" + tag)
                .toList();

        List<String> successfulDeleted = imageNames.parallelStream()
                .map(imageName -> {
                    try {
                        return getVersionsForImage(project, location, repository, imageName);
                    } catch (IOException e) {
                        logService.publish("Unable to fetch versions for " + imageName, LoggingLevel.ERROR);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(imageVersions -> imageVersions.versions().getFirst())
                .map(imageVersionDto -> {
                    try {
                       return getImageNameAndVersion(imageVersionDto);
                    }
                    catch (Exception e) {
                        logService.publish(String.format("Could not parse and find image name and version for %s", imageVersionDto.name()), LoggingLevel.ERROR);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(imageNameAndVersion -> deleteImage(project, location, repository, imageNameAndVersion.getLeft(), imageNameAndVersion.getRight(), bearerHeader))
                .filter(message -> message.equals("success"))
                .toList();

        if (successfulDeleted.isEmpty()) {
            stopWatch.stop();
            logService.publish(String.format("No images were successfully deleted for %s. Runtime: %s", repository, stopWatch.getTime()), LoggingLevel.WARN);
            throw new ClientWebApplicationException(String.format("No images were successfully deleted for %s. Runtime: %s", repository, stopWatch.getTime()));
        }

        stopWatch.stop();
        logService.publish(String.format("Successfully began deletion process for %d stale images for %s. Runtime: %s", successfulDeleted.size(), repository, stopWatch.getTime()), LoggingLevel.INFO);
    }

    public String deleteImage(String project, String location, String repository, String imageName, String version, String bearerHeader){
        try {
            logService.publish(String.format("Attempting to begin deletion for image: %s from repository: %s", imageName, repository), LoggingLevel.INFO);
            String res = artifactRegistryRestClient.deleteImage(project, location, repository, imageName, version, bearerHeader);
            return "success";
        } catch (Exception e) {
            logService.publish(String.format("Failed to begin deletion for image: %s from repository: %s\n%s", imageName, repository, e.getMessage()), LoggingLevel.ERROR);
            return "failure";
        }
    }

    private Pair<String, String> getImageNameAndVersion(ImageVersionDto imageVersionDto) {
        String name = imageVersionDto.name();
        String[] nameSplit = name.split("/");
        String imageName = "";
        String version = "";
        for (String section: nameSplit) {
            if (section.matches("image-\\d+")) {
                imageName = section;
            }
            if (section.startsWith("sha256:")) {
                version = section;
            }
        }
        if (imageName.isBlank() || version.isBlank()) {
            throw new RuntimeException("Could not find image name or version inside of " + imageName);
        }

        return new ImmutablePair<>(imageName, version);
    }

    private String getImageTimeTag(ArtifactRegistryPackage artifactRegistryPackage) {
        String entireImageName = artifactRegistryPackage.name();
        String actualImageName = Arrays.stream(entireImageName.split("/"))
                .toList()
                .getLast();
        int indexOfDash = actualImageName.indexOf('-');
        if (indexOfDash == -1) {
            return null;
        }
        return actualImageName.substring(indexOfDash + 1);
    }

    public ImageVersions getVersionsForImage(String project, String location, String repository, String packageName) throws IOException {
        logService.publish("Fetching versions for " + packageName, LoggingLevel.INFO);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String bearerHeader = getServiceAccessKeyHeader();

        String response = artifactRegistryRestClient.listVersions(project, location, repository, packageName, bearerHeader);
        ImageVersions imageVersions = objectMapper.readValue(response, ImageVersions.class);
        stopWatch.stop();
        logService.publish(String.format("Successfully fetched versions for %s. Runtime: %s", packageName, stopWatch.getTime()), LoggingLevel.INFO);
        return imageVersions;
    }

    private String getServiceAccessKeyHeader() throws IOException {
        String serviceAccessKey = gcpServiceAccessKeyDecoder.getServiceAccessKey();
        return "Bearer " + serviceAccessKey;
    }
}
