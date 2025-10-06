package ineedhousing.cronjob.new_listings_service;

import ineedhousing.cronjob.new_listings_service.models.NewListingsCollectionEvent;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "new-listings")
public interface NewListingsServiceRestClient {

    @POST
    @Path("/v1/webhooks/new-listings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String newListingsWebhook(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName, NewListingsCollectionEvent newListingsCollectionEvent);
}
