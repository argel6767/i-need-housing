package com.ineedhousing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.models.SuccessfulKeyRotationEvent;
import com.ineedhousing.rest_clients.MainAPIEmailServiceRestClient;
import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

public class KeyRotationSubscriber {

    @Inject
    @RestClient
    MainAPIEmailServiceRestClient mainAPIEmailServiceRestClient;

    @Inject
    Config config;

    @Inject
    ObjectMapper objectMapper;

    private String apiToken;
    private String serviceName;

    @PostConstruct
    public void init() {
        apiToken = config.getOptionalValue("keymaster_service.api.token", String.class)
                .orElseThrow(() -> new IllegalStateException("keymaster_service API token not present!"));
        serviceName =  config.getOptionalValue("keymaster_service.service.name", String.class)
                .orElseThrow(() -> new IllegalStateException("keymaster_service service name not present!"));
    }

    public void notifyNewKeyRotation(@ObservesAsync SuccessfulKeyRotationEvent successfulKeyRotationEvent) {
        try {
            String eventJson = stringify(successfulKeyRotationEvent);
            mainAPIEmailServiceRestClient.notifyNewKeyRotation(apiToken, serviceName, eventJson);
            Log.info("Notify New Key Rotation Event Successfully!");
        }
        catch (ClientWebApplicationException e) {
            Log.error("Notify New Key Rotation Event Failed! Cause: " +  e.getResponse().getStatusInfo().getReasonPhrase());
        }
    }

    private String stringify(SuccessfulKeyRotationEvent successfulKeyRotationEvent) {
        try {
            return objectMapper.writeValueAsString(successfulKeyRotationEvent);
        }
        catch (Exception e) {
            Log.error("Failed to parse: " + e.getMessage());
            return  e.getMessage();
        }
    }
}
