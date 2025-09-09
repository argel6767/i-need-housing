package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.events.LegacyEmailEvent;
import com.ineedhousing.new_listings_service.models.events.NewDataSuccessfullyFetchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final RestClient restClient;
    private final ApplicationEventPublisher eventPublisher;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);


    public EmailService(@Qualifier("email_service") RestClient restClient, ApplicationEventPublisher eventPublisher) {
        this.restClient = restClient;
        this.eventPublisher = eventPublisher;
    }

    @EventListener(NewDataSuccessfullyFetchedEvent.class)
    @Async
    public void onNewDataSuccessfullyFetched(NewDataSuccessfullyFetchedEvent event) {
        sendNewListingsMadeEmail(event);
    }

    public void sendNewListingsMadeEmail(NewDataSuccessfullyFetchedEvent event) {
        logger.info("Sending email request to Email Service");
        try {
            String response = restClient.post()
                    .uri("/v1/emails/services/new-listings-made")
                    .body(event)
                    .retrieve()
                    .body(String.class);
            if (response == null) {
                logger.warn("Response was null, check Email Service logs, sending to legacy email service in case");
                eventPublisher.publishEvent(new LegacyEmailEvent(event, LocalDateTime.now()));
            }
            logger.info("Email request sent to Email Service. Response: {}", response);
        }
        catch (Exception e) {
            logger.error("Failed to send request to email service. Error message {}. Falling back to legacy email service", e.getMessage());
            eventPublisher.publishEvent(new LegacyEmailEvent(event, LocalDateTime.now()));
        }
    }
}
