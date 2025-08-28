package ineedhousing.cronjob.gcp.artifact_registry;

import ineedhousing.cronjob.gcp.models.GetImagesDto;
import ineedhousing.cronjob.gcp.models.ImageVersionDto.*;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

@Path("/artifact-registries")
public class ArtifactRegistryResource {

    @Inject
    ArtifactRegistryService artifactRegistryService;

    @GET
    @Path("/images")
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public GetImagesDto getImages(@QueryParam("project") String project,
                                  @QueryParam("location") String location,
                                  @QueryParam("repository") String repository) throws IOException {
        if (!ObjectUtils.allNotNull(project, location, repository)) {
            throw new BadRequestException("One or more required parameters are missing");
        }
        return artifactRegistryService.getImages(project, location, repository);
    }

    @GET
    @Path("/images/versions")
    public ImageVersions getImagesVersions(@QueryParam("project") String project,
                                    @QueryParam("location") String location,
                                    @QueryParam("repository") String repository,
                                    @QueryParam("image") String image) throws IOException {
        if (!ObjectUtils.allNotNull(project, location, repository, image)) {
            throw new BadRequestException("One or more required parameters are missing");
        }
        return artifactRegistryService.getVersionsForImage(project, location, repository, image);
    }

    @DELETE
    @Path("/images")
    public void deleteStaleImages(@QueryParam("project") String project,
                                  @QueryParam("location") String location,
                                  @QueryParam("repository") String repository) throws IOException {
        if (!ObjectUtils.allNotNull(project, location, repository)) {
            throw new BadRequestException("One or more required parameters are missing");
        }
        artifactRegistryService.deleteOldImages(project, location, repository);
    }
}


