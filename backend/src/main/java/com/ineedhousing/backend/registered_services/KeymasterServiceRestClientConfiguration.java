package com.ineedhousing.backend.registered_services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class KeymasterServiceRestClientConfiguration {

    @Value("${service.api.token}")
    private String serviceApiToken;

    @Value("${service.name}")
    private String serviceName;

    @Value("${key.master.service.url}")
    private String keymasterServiceUrl;

    @Bean(name = "keymaster_service")
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl(keymasterServiceUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", serviceApiToken);
                    httpHeaders.set("X-Service-Name", serviceName);
                }).build();
    }
}
