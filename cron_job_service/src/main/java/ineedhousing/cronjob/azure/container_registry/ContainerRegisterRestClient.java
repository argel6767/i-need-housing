package ineedhousing.cronjob.azure.container_registry;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/v2")
@RegisterRestClient(configKey = "acr-api")
public interface ContainerRegisterRestClient {

    // List all repositories in your registry
    @GET
    @Path("/_catalog")
    @Produces(MediaType.APPLICATION_JSON)
    String listRepositories(@HeaderParam("Authorization") String authHeader);

    // List all tags for a specific repository
    @GET
    @Path("/{repository}/tags/list")
    @Produces(MediaType.APPLICATION_JSON)
    String listTags(
            @PathParam("repository") String repository,
            @HeaderParam("Authorization") String authHeader
    );

    // Get manifest with digest in response headers
    @GET
    @Path("/{repository}/manifests/{tag}")
    Response getManifestWithHeaders(
            @PathParam("repository") String repository,
            @PathParam("tag") String tag,
            @HeaderParam("Authorization") String authHeader,
            @HeaderParam("Accept") String accept
    );

    // Delete an image by digest
    @DELETE
    @Path("/{repository}/manifests/{digest}")
    void deleteManifest(
            @PathParam("repository") String repository,
            @PathParam("digest") String digest,
            @HeaderParam("Authorization") String authHeader
    );
}
