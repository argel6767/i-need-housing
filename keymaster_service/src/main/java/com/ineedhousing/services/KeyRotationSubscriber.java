package com.ineedhousing.services;

import com.ineedhousing.models.SuccessfulKeyRotationEvent;
import com.ineedhousing.rest_clients.MainAPIEmailServiceRestClient;
import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

public class KeyRotationSubscriber {

    @Inject
    @RestClient
    MainAPIEmailServiceRestClient mainAPIEmailServiceRestClient;

    @Inject
    Config config;

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
        mainAPIEmailServiceRestClient.notifyNewKeyRotation(apiToken, serviceName, successfulKeyRotationEvent);
        Log.info("Notify New Key Rotation Successfully!");
    }
}
