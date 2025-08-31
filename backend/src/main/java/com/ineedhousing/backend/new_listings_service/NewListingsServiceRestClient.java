package com.ineedhousing.backend.new_listings_service;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NewListingsServiceRestClient {

    private final ServiceInteractionConstants serviceInteractionConstants;

    public NewListingsServiceRestClient(ServiceInteractionConstants serviceInteractionConstants) {
        this.serviceInteractionConstants = serviceInteractionConstants;
    }

    @Bean(name = "new listings service")
    RestClient restClient() {
        return RestClient.builder().baseUrl(serviceInteractionConstants.getNewListingsServiceUrl())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", serviceInteractionConstants.getApiToken());
                    httpHeaders.set("X-Service-Name", serviceInteractionConstants.getServiceName());
                })
                .build();
    }
}
