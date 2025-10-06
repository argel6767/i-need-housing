package com.ineedhousing.new_listings_service.subscribers;


import com.ineedhousing.new_listings_service.models.events.new_listings.AirbnbCollectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AirbnbSubscriber {

    Logger logger = LoggerFactory.getLogger(AirbnbSubscriber.class);

    @EventListener
    @Async
    public void handleNewListingsEvent(AirbnbCollectionEvent event) {
    }
}
