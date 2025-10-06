package com.ineedhousing.backend.email.v2;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestClient;

@Configuration
@Lazy
public class EmailServiceRestClientConfiguration {

    @Bean("email_service")
    RestClient restClient(ServiceInteractionConstants serviceInteractionConstants) {
        return RestClient.builder()
                .baseUrl(serviceInteractionConstants.getEmailServiceUrl())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", serviceInteractionConstants.getApiToken());
                    httpHeaders.set("X-Service-Name", serviceInteractionConstants.getServiceName());
                }).build();
    }
}
