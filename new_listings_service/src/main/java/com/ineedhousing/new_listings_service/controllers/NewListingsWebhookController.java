package com.ineedhousing.new_listings_service.controllers;

import com.ineedhousing.new_listings_service.models.NewListingsEvent;
import com.ineedhousing.new_listings_service.services.NewListingsEventPublisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/webhooks")
public class NewListingsWebhookController {

    private final NewListingsEventPublisher eventPublisher;

    public NewListingsWebhookController(NewListingsEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/new-listings")
    public ResponseEntity<String> publishNewListingsEvent(NewListingsEvent newListingsEvent) {
        eventPublisher.publishEvent(newListingsEvent);
        return ResponseEntity.status(202).body("New Listings Event Published");
    }
}
