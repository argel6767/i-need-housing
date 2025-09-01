package com.ineedhousing.backend.keymaster_service;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class KeymasterServiceRestClientConfiguration {

    private final ServiceInteractionConstants serviceInteractionConstants;

    public KeymasterServiceRestClientConfiguration(ServiceInteractionConstants serviceInteractionConstants) {
        this.serviceInteractionConstants = serviceInteractionConstants;
    }

    @Bean(name = "keymaster_service")
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl(serviceInteractionConstants.getKeymasterServiceUrl())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", serviceInteractionConstants.getApiToken());
                    httpHeaders.set("X-Service-Name", serviceInteractionConstants.getServiceName());
                }).build();
    }
}
