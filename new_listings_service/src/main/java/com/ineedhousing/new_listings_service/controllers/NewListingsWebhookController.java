package com.ineedhousing.new_listings_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.new_listings_service.models.requests.NewListingsEventDto;
import com.ineedhousing.new_listings_service.services.NewListingsEventPublisher;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/v1/webhooks")
@RateLimiter(name = "webhooks")
public class NewListingsWebhookController {

    private final NewListingsEventPublisher eventPublisher;

    public NewListingsWebhookController(NewListingsEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/new-listings")
    public ResponseEntity<String> publishNewListingsEvent(NewListingsEventDto newListingsEventDto) {
        eventPublisher.publishEvent(newListingsEventDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("New Listings Event Published");
    }
}
