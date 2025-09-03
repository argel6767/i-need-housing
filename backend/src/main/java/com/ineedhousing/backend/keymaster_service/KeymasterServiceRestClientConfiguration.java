package com.ineedhousing.backend.keymaster_service;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class KeymasterServiceRestClientConfiguration {

    @Bean(name = "keymaster_service")
    RestClient restClient(ServiceInteractionConstants constants) {
        return RestClient.builder()
                .baseUrl(constants.getKeymasterServiceUrl())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", constants.getApiToken());
                    httpHeaders.set("X-Service-Name", constants.getServiceName());
                }).build();
    }
}
