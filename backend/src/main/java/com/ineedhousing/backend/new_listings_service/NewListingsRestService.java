package com.ineedhousing.backend.new_listings_service;

import com.ineedhousing.backend.ping_services.models.models.PingAllServicesEvent;
import com.ineedhousing.backend.ping_services.models.models.PingServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingNewListingsServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class NewListingsRestService {

    private final RestClient restClient;

    public NewListingsRestService(@Qualifier("new listings service") RestClient restClient) {
        this.restClient = restClient;
    }

    public void pingService() {
        try {
            String response = restClient.post()
                    .uri("/ping")
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);
            log.info(response);
        }
        catch (RestClientException e) {
            log.warn("Service was pinged but overall request was not successful: {},", e.getMessage());
        }
        catch (Exception e) {
            log.error("Service was unsuccessful: {},", e.getMessage());
        }
    }

    @EventListener
    @Async
    public void onPingAllServicesEvent(PingAllServicesEvent pingAllServicesEvent) {
        log.info("Pinging New Listings Service during all services pinged event");
        pingService();
    }

    @EventListener
    @Async
    public void onPingServiceEvent(PingNewListingsServiceEvent pingServiceEvent) {
        log.info("Pinging New Listings Service");
        pingService();
    }
}
