package ineedhousing.cronjob.new_listings_service;

import ineedhousing.cronjob.new_listings_service.models.NewListingsCollectionEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class NewListingsEventPublisher {

    @Inject
    Event<NewListingsCollectionEvent> publisher;

    public void publishNewListingEvent(String message) {
        publisher.fireAsync(new NewListingsCollectionEvent(message, LocalDateTime.now()));
    }
}
