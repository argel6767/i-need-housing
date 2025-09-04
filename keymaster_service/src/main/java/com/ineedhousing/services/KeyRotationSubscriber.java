package com.ineedhousing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.models.SuccessfulKeyRotationEvent;
import com.ineedhousing.rest_clients.MainAPIEmailServiceRestClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
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

    public void notifyNewKeyRotation(@ObservesAsync SuccessfulKeyRotationEvent successfulKeyRotationEvent) {
        try {
            if (apiToken == null) {
                apiToken = config.getOptionalValue("keymaster_service.api.token", String.class)
                        .orElseThrow(() -> new IllegalStateException("keymaster_service API token not present!"));
            }
            if (serviceName == null) {
                serviceName =  config.getOptionalValue("keymaster_service.service.name", String.class)
                        .orElseThrow(() -> new IllegalStateException("keymaster_service service name not present!"));
            }
            Map<String, String> dto = Map.of("message", successfulKeyRotationEvent.message(), "newKey", successfulKeyRotationEvent.newKey());
            String dtoJson = objectMapper.writeValueAsString(dto);
            mainAPIEmailServiceRestClient.notifyNewKeyRotation(apiToken, serviceName, dtoJson);
            Log.info("Notify New Key Rotation Event Successfully!");
        }
        catch (WebApplicationException e) {
            Log.error("Notify New Key Rotation Event Failed! Cause: " +  e.getResponse().getStatusInfo().getReasonPhrase());
            Log.error("Logging response: " + e.getResponse().readEntity(String.class));
        }
        catch (Exception e) {
            Log.error("Notify New Key Rotation Event Failed! Cause: " + e.getMessage());
        }
    }
}
