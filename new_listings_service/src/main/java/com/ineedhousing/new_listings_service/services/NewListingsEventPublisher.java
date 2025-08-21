package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.requests.NewListingsEventDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class NewListingsEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public NewListingsEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(NewListingsEventDto event) {
        eventPublisher.publishEvent(event);
    }
}
