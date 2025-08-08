package ineedhousing.cronjob.azure.container_registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import ineedhousing.cronjob.azure.container_registry.model.BulkDigestsDto;
import ineedhousing.cronjob.azure.container_registry.model.DigestDto;
import ineedhousing.cronjob.azure.container_registry.model.ManifestsDeletedDto;
import ineedhousing.cronjob.azure.container_registry.model.TagsDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/container-registry")
public class ContainerRegistryResource {

    @Inject
    ContainerRegistryRestService containerRegistryRestService;

    @GET
    @Path("/repositories")
    @Produces(MediaType.APPLICATION_JSON)
    public String listRepositories() {
        return containerRegistryRestService.getRepositories();
    }

    // GET /acr/myapp/tags
    @GET
    @Path("/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public TagsDto listTags(@QueryParam("repository") String repository) throws JsonProcessingException {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        return containerRegistryRestService.listTags(repository);
    }

    @GET
    @Path("/manifests")
    @Produces(MediaType.APPLICATION_JSON)
    public DigestDto.CompleteManifestInfoDto getManifest(@QueryParam("repository") String repository, @QueryParam("tag") String tag) throws JsonProcessingException {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        if (tag == null || tag.isEmpty()) {
            throw new BadRequestException("Tag parameter is required");
        }
        return containerRegistryRestService.getManifestByTag(repository, tag);
    }

    @POST
    @Path("/manifests")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DigestDto.CompleteManifestInfoDto> getManifestsByTags(BulkDigestsDto request) {
        return containerRegistryRestService.getManifestsByTags(request.repository(), request.tags());
    }


    @DELETE
    @Path("/manifests")
    public void deleteManifest(@QueryParam("repository") String repository, @QueryParam("digest") String digest) {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        if (digest == null || digest.isEmpty()) {
            throw new BadRequestException("Tag parameter is required");
        }
        containerRegistryRestService.deleteManifest(repository, digest);
    }

    @DELETE
    @Path("/manifests/bulk")
    public ManifestsDeletedDto deleteOldManifests(@QueryParam("repository") String repository) throws JsonProcessingException {
        if (repository == null || repository.isEmpty()) {
            throw new BadRequestException("Repository parameter is required");
        }
        return containerRegistryRestService.deleteOldImages(repository);
    }
}
