package ineedhousing.cronjob.gcp.artifact_registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.gcp.GCPServiceAccessKeyDecoder;
import ineedhousing.cronjob.gcp.models.ArtifactRegistryPackage;
import ineedhousing.cronjob.gcp.models.GetImagesDto;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.util.Arrays;
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
        String bearerHeader = getServiceAccessKeyHeader();
        String response =  artifactRegistryRestClient.listImages(project, location, repository, bearerHeader);
        GetImagesDto responseObject =  objectMapper.readValue(response, GetImagesDto.class);
        stopWatch.stop();
        logService.publish(String.format("%d image names fetched for %s. Runtime %s", responseObject.packages().size(), repository, stopWatch.getTime()), LoggingLevel.INFO);
        return  responseObject;
    }

    public void deleteOldImages(String project, String location, String repository) throws IOException {
        logService.publish("Deleting stale images for " + repository, LoggingLevel.INFO);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String bearerHeader = getServiceAccessKeyHeader();
        GetImagesDto packages = getImages(project, location, repository);
        List<String> tags = packages.packages().stream()
                .map(this::getImageTimeTag)
                .filter(Objects::nonNull)
                .toList();

        if (tags.size() < 4) {
            stopWatch.stop();
            logService.publish("Minimum amount of images present, skipping deletion. Runtime: " + stopWatch.getTime(), LoggingLevel.INFO);
        }

        tags.parallelStream()
                .forEach(tag -> artifactRegistryRestClient.deleteImage(project, location, repository, tag, bearerHeader));
        stopWatch.stop();
        logService.publish(String.format("Deleted %d stale images for %s. Runtime: %s", tags.size(), repository, stopWatch.getTime()), LoggingLevel.INFO);
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
        return actualImageName.substring(indexOfDash);
    }



    private String getServiceAccessKeyHeader() throws IOException {
        String serviceAccessKey = gcpServiceAccessKeyDecoder.getServiceAccessKey();
        return "Bearer " + serviceAccessKey;
    }
}
