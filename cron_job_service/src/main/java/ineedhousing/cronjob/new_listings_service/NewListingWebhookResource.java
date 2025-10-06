package ineedhousing.cronjob.new_listings_service;

import ineedhousing.cronjob.new_listings_service.models.NewListingsCollectionEvent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.time.LocalDateTime;

@Path("/new-listings")
public class NewListingWebhookResource {

    @Inject
    Event<NewListingsCollectionEvent> event;

    @POST
    @Path("/webhook-trigger")
    public String manuallyTriggerWebHook() {
        event.fireAsync(new NewListingsCollectionEvent("Manual webhook trigger from hitting endpoint", LocalDateTime.now()));
        return "Successfully triggered webhook!";
    }
}
