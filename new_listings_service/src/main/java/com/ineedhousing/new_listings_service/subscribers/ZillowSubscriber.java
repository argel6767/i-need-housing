package com.ineedhousing.new_listings_service.subscribers;

import com.ineedhousing.new_listings_service.models.NewListingsEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ZillowSubscriber {

    @EventListener
    @Async
    public void handleNewListingsEvent(NewListingsEvent event) {

    }
}
