package com.ineedhousing.new_listings_service.services;

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

    @EventListener(NewDataSuccessfullyFetchedEvent.class)
    @Async
    public void onNewDataSuccessfullyFetchedEvent(NewDataSuccessfullyFetchedEvent event) {
        sendNewListingsMadeEmail(event);
    }

    public void sendNewListingsMadeEmail(NewDataSuccessfullyFetchedEvent event) {
        try {
            restClient.post()
                    .uri("/emails/")
        }
    }
}
