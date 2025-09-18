package com.ineedhousing.services;

import io.quarkus.websockets.next.HandshakeRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;

@ApplicationScoped
public class WebSocketAuthService {

    @Inject
    ApiTokenValidationService apiTokenValidationService;

    /**
     * Authenticates during handhake using service API key and service name
     * @param handshakeRequest
     * @return
     */
    public boolean isAuthenticated(HandshakeRequest handshakeRequest) {
        String apiToken = handshakeRequest.header("X-Api-Token");
        String serviceName = handshakeRequest.header("X-Service-Name");
        if (ObjectUtils.allNull(apiToken, serviceName)) {
            return false;
        }
        return apiTokenValidationService.isServiceAuthenticated(apiToken, serviceName);
    }

}
