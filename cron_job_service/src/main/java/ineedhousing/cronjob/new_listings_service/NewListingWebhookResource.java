package ineedhousing.cronjob.new_listings_service;

import ineedhousing.cronjob.new_listings_service.models.NewListingEvent;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.time.LocalDateTime;

@Path("/new-listings")
public class NewListingWebhookResource {

    @Inject
    NewListingsWebhookService newListingsWebhookService;

    @POST
    @Path("/webhook-trigger")
    public String manuallyTriggerWebHook() {
        newListingsWebhookService.onSuccessfulListingsDeletion(new NewListingEvent("Manual webhook trigger from hitting endpoint", LocalDateTime.now()));
        return "Successfully triggered webhook!";
    }
}
