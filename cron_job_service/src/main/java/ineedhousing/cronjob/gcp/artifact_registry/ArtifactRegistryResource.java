package ineedhousing.cronjob.gcp.artifact_registry;

import ineedhousing.cronjob.gcp.models.GetImagesDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

@Path("/artifact-registries")
public class ArtifactRegistryResource {

    @Inject
    ArtifactRegistryService artifactRegistryService;

    @GET
    @Path("/images")
    public GetImagesDto getImages(@QueryParam("project") String project, @QueryParam("location") String location, @QueryParam("repository") String repository) throws IOException {
        if (!ObjectUtils.allNotNull(project, location, repository)) {
            throw new BadRequestException("One or more required parameters are missing");
        }
        return artifactRegistryService.getImages(project, location, repository);
    }

    @DELETE
    @Path("/images")
    public void deleteStaleImages(@QueryParam("project") String project, @QueryParam("location") String location, @QueryParam("repository") String repository) throws IOException {
        if (!ObjectUtils.allNotNull(project, location, repository)) {
            throw new BadRequestException("One or more required parameters are missing");
        }
        artifactRegistryService.deleteOldImages(project, location, repository);
    }
}


