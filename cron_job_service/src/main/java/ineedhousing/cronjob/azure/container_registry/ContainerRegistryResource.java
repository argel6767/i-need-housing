package ineedhousing.cronjob.azure.container_registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import ineedhousing.cronjob.azure.container_registry.models.BulkDigestsDto;
import ineedhousing.cronjob.azure.container_registry.models.DigestDto;
import ineedhousing.cronjob.azure.container_registry.models.ManifestsDeletedDto;
import ineedhousing.cronjob.azure.container_registry.models.TagsDto;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/container-registries")
public class ContainerRegistryResource {

    @Inject
    ContainerRegistryService containerRegistryService;

    @GET
    @Path("/repos")
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 10, window = 3, windowUnit = ChronoUnit.MINUTES)
    public String listRepositories() {
        return containerRegistryService.getRepositories();
    }

    // GET /acr/myapp/tags
    @GET
    @Path("/tags")
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public TagsDto listTags(@QueryParam("repository") String repository) throws JsonProcessingException {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        return containerRegistryService.listTags(repository);
    }

    @GET
    @Path("/manifests")
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public DigestDto.CompleteManifestInfoDto getManifest(@QueryParam("repository") String repository, @QueryParam("tag") String tag) throws JsonProcessingException {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        if (tag == null || tag.isEmpty()) {
            throw new BadRequestException("Tag parameter is required");
        }
        return containerRegistryService.getManifestByTag(repository, tag);
    }

    @POST
    @Path("/manifests")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public List<DigestDto.CompleteManifestInfoDto> getManifestsByTags(BulkDigestsDto request) {
        return containerRegistryService.getManifestsByTags(request.repository(), request.tags());
    }

    @DELETE
    @Path("/manifests")
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public void deleteManifest(@QueryParam("repository") String repository, @QueryParam("digest") String digest) {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        if (digest == null || digest.isEmpty()) {
            throw new BadRequestException("Tag parameter is required");
        }
        containerRegistryService.deleteManifest(repository, digest);
    }

    @DELETE
    @Path("/manifests/bulk")
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public ManifestsDeletedDto deleteOldManifests(@QueryParam("repository") String repository) throws JsonProcessingException {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        return containerRegistryService.deleteOldImages(repository);
    }
}
