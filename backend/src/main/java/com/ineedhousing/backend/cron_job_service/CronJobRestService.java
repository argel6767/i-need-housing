package com.ineedhousing.backend.cron_job_service;

import com.ineedhousing.backend.cron_job_service.model.HealthPing;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
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
