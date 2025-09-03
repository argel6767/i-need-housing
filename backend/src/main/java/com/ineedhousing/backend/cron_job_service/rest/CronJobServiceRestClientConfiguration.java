package com.ineedhousing.backend.cron_job_service.rest;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CronJobServiceRestClientConfiguration {

    @Bean(name = "Cron Job Service Client")
    public RestClient restClient(ServiceInteractionConstants constants) {
        return RestClient.builder()
                .baseUrl(constants.getCronJobServiceUrl())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", constants.getApiToken());
                    httpHeaders.set("X-Service-Name", constants.getServiceName());
                })
                .build();
    }
}
