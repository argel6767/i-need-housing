package com.ineedhousing.backend.cron_job_service.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CronJobServiceRestClientConfiguration {

    @Value("${cron.job.service.url}")
    private String serviceUrl;

    @Value("${service.api.token}")
    private String apiToken;

    @Value("service.name")
    private String serviceName;

    @Bean(name = "Cron Job Service Client")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(serviceUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", apiToken);
                    httpHeaders.set("X-Service-Name", serviceName);
                })
                .build();
    }
}
