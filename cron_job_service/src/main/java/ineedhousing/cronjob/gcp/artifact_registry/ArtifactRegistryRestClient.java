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
    String listImages(@PathParam("project") String project,
                      @PathParam("location") String location,
                      @PathParam("repository") String repositories,
                      @HeaderParam("Authorization") String serviceAccessKey);

    @DELETE
    @Path("/{project}/locations/{location}/repositories/{repository}/packages/{package}/versions/{version}")
    String deleteImage(@PathParam("project") String project,
                       @PathParam("location") String location,
                       @PathParam("repository") String repositories,
                       @PathParam("package") String packageName,
                       @PathParam("version") String version,
                       @HeaderParam("Authorization") String serviceAccessKey);

    @GET
    @Path("/{project}/locations/{location}/repositories/{repository}/packages/{packageName}/versions")
    @Produces(MediaType.APPLICATION_JSON)
    String listVersions(@PathParam("project") String project,
                        @PathParam("location") String location,
                        @PathParam("repository") String repository,
                        @PathParam("packageName") String packageName,
                        @HeaderParam("Authorization") String serviceAccessKey);
}
