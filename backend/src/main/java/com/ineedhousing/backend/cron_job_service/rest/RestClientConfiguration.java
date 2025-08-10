package com.ineedhousing.backend.cron_job_service.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Value("${cron.job.service.url}")
    private String serviceUrl;

    @Value("${cron.job.service.access.header}")
    private String accessHeader;

    @Bean(name = "Cron Job Service Client")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(serviceUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.set("Access-Header", accessHeader))
                .build();
    }
}
