package com.ineedhousing.backend.new_listings_service;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NewListingsServiceRestClient {

    @Bean(name = "new listings service")
    RestClient restClient(ServiceInteractionConstants constants) {
        return RestClient.builder().baseUrl(constants.getNewListingsServiceUrl())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", constants.getApiToken());
                    httpHeaders.set("X-Service-Name", constants.getServiceName());
                })
                .build();
    }
}
