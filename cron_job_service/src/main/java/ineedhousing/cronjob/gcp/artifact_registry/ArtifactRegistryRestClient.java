package ineedhousing.cronjob.gcp.artifact_registry;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("v1/projects")
@RegisterRestClient(configKey = "gcp-ar")
public interface ArtifactRegistryRestClient {

    @GET
    @Path("/{project}/locations/{location}/repositories/{repository}/packages")
    @Produces(MediaType.APPLICATION_JSON)
    String listImages(@PathParam("project") String project, @PathParam("location") String location, @PathParam("repository") String repositories, @HeaderParam("Authorization") String serviceAccessKey);

    @DELETE
    @Path("/{project}/locations/{location}/repositories/{repository}/packages/versions/{version}")
    void deleteImage(@PathParam("project") String project, @PathParam("location") String location, @PathParam("repository") String repositories, @PathParam("version") String version, @HeaderParam("Authorization") String serviceAccessKey);
}
