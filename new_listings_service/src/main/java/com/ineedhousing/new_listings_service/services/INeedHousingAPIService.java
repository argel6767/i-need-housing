package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.events.LegacyEmailEvent;
import com.ineedhousing.new_listings_service.models.events.NewDataSuccessfullyFetchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class INeedHousingAPIService {

    private final RestClient restClient;
    private final Logger logger = LoggerFactory.getLogger(INeedHousingAPIService.class);

    public INeedHousingAPIService(@Qualifier("ineedhousing_api") RestClient restClient) {
        this.restClient = restClient;
    }

    @EventListener(LegacyEmailEvent.class)
    @Async
    public void onLegacyEmailEvent(LegacyEmailEvent event) {
        sendNewListingsMadeEmail(event.event());
    }

    public void sendNewListingsMadeEmail(NewDataSuccessfullyFetchedEvent event) {
        try {
            String response = restClient.post()
                    .uri("/emails/notifications/new-listings")
                    .body(event)
                    .retrieve()
                    .body(String.class);
            if (response == null) {
                logger.warn("New listings email request failed to be sent to INeedHousing API");
            }
            else {
                logger.info(response);
            }
        }
        catch (Exception e) {
            logger.error("Failed to send New Listings email request to ineedHousing API. Error message {}" , e.getMessage());
        }
    }
}