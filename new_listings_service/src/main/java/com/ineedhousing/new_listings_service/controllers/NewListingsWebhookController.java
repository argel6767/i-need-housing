package com.ineedhousing.new_listings_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.new_listings_service.models.events.NewListingsEvent;
import com.ineedhousing.new_listings_service.services.NewListingsEventPublisher;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Deprecated
@RestController
@RequestMapping("/v1/webhooks")
@RateLimiter(name = "webhooks")
public class NewListingsWebhookController {

    private final NewListingsEventPublisher eventPublisher;

    public NewListingsWebhookController(NewListingsEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/new-listings")
    public ResponseEntity<String> publishNewListingsEvent(NewListingsEvent newListingsEvent) {
        eventPublisher.publishEvent(newListingsEvent);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("New Listings Event Published. This webhook is no longer used and is considered deprecated. All collection events are now handled via Azure ServiceBus");
    }
}
