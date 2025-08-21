package com.ineedhousing.new_listings_service.subscribers;

import com.ineedhousing.new_listings_service.models.requests.NewListingsEventDto;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AirbnbSubscriber {

    @EventListener
    @Async
    public void handleNewListingsEvent(NewListingsEventDto event) {

    }
}
