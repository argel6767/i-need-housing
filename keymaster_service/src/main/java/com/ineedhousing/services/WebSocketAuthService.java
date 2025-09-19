package com.ineedhousing.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.websockets.next.HandshakeRequest;

@ApplicationScoped
public class WebSocketAuthService {

    @Inject
    ApiTokenValidationService apiTokenValidationService;


    /**
     * Checks for access token in Access-Header token
     * @param handshakeRequest
     * @return
     */
    public boolean isAuthenticated(HandshakeRequest handshakeRequest) {
        String apiToken = handshakeRequest.header("X-Api-Token");
        String serviceName = handshakeRequest.header("X-Service-Name");
        if (apiToken == null || serviceName == null) {
            return false;
        }
        return apiTokenValidationService.isServiceAuthorized(apiToken, serviceName);
    }

}
