package com.ineedhousing.backend.cron_job_service.rest;

import com.ineedhousing.backend.cron_job_service.model.HealthPing;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.ping_services.models.models.PingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class CronJobRestService {

    private final RestClient restClient;

    public CronJobRestService(@Qualifier("Cron Job Service Client") RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Pings the service while getting back a health check
     * @return
     */
    public HealthPing pingService() {
        HealthPing successfulPing = restClient
                .get()
                .uri("/ping")
                .retrieve()
                .body(HealthPing.class);
        return successfulPing;
    }

    @EventListener
    @Async
    public void pingService(PingEvent pingEvent) {
        log.info("Pinging New Listings Service");
        pingService();
    }

    /**
     * Fetches the most recent 'limit' logs
     * @param limit
     * @return
     */
    public LogEventResponse getMostRecentLogs(int limit) {
        LogEventResponse successfulLogRequest = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/logs")
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(LogEventResponse.class);
        return successfulLogRequest;
    }

    /**
     * Fetches the set max most recent saved logs
     * @return
     */
    public LogEventResponse getMostRecentLogs() {
        LogEventResponse successfulLogRequest = restClient
                .get()
                .uri("/logs")
                .retrieve()
                .body(LogEventResponse.class);
        return successfulLogRequest;
    }

}
