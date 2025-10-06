package com.ineedhousing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.models.enums.LoggingLevel;
import com.ineedhousing.models.events.NewServiceRegisteredEvent;
import com.ineedhousing.models.events.SuccessfulKeyRotationEvent;
import com.ineedhousing.rest_clients.EmailServiceRestClient;
import com.ineedhousing.rest_clients.MainAPIEmailServiceRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
public class EmailEventsSubscriber {

    @Inject
    @RestClient
    MainAPIEmailServiceRestClient mainAPIEmailServiceRestClient;

    @Inject
    @RestClient
    EmailServiceRestClient emailServiceRestClient;

    @Inject
    LogService logService;

    @Inject
    Config config;

    @Inject
    ObjectMapper objectMapper;

    private String apiToken;
    private String serviceName;

    @Deprecated
    public void notifyNewKeyRotation(@ObservesAsync SuccessfulKeyRotationEvent successfulKeyRotationEvent) {
        setServiceInteractionValues();
        try {
            Map<String, String> dto = Map.of("message", successfulKeyRotationEvent.message(), "newKey", successfulKeyRotationEvent.newKey());
            String dtoJson = objectMapper.writeValueAsString(dto);
            try {
                emailServiceRestClient.notifyNewKeyRotation(apiToken, serviceName, dtoJson);
            }
            catch (WebApplicationException e) {
                logService.publish("Email service request service failed. Falling back to legacy INeedHousing API email service. Error message: " + e.getMessage(), LoggingLevel.ERROR);
                mainAPIEmailServiceRestClient.notifyNewKeyRotation(apiToken, serviceName, dtoJson);
            }
            logService.publish("Notify New Key Rotation Event Successfully!", LoggingLevel.INFO);
        }
        catch (WebApplicationException e) {
            logService.publish("Notify New Key Rotation Event Failed! Cause: " +  e.getResponse().getStatusInfo().getReasonPhrase(), LoggingLevel.ERROR);
            logService.publish("Logging response: " + e.getResponse().readEntity(String.class), LoggingLevel.ERROR);
        }
        catch (Exception e) {
            logService.publish("Notify New Key Rotation Event Failed! Cause: " + e.getMessage(), LoggingLevel.ERROR);
        }
    }

    public void notifyNewServiceRegistered(@ObservesAsync NewServiceRegisteredEvent newServiceRegisteredEvent) {
        setServiceInteractionValues();
        try {
            String dtoJson = objectMapper.writeValueAsString(newServiceRegisteredEvent);
            logService.publish("Sending the following dto: " + dtoJson, LoggingLevel.INFO);
            emailServiceRestClient.notifyNewServiceRegistration(apiToken, serviceName, dtoJson);
            logService.publish("Notify New Service Registered Event Successfully!", LoggingLevel.INFO);
        }
        catch (WebApplicationException e) {
            logService.publish("Notify New Service Registered Event Failed! Cause: " +  e.getResponse().getStatusInfo().getReasonPhrase(), LoggingLevel.ERROR);
            logService.publish("Logging response: " + e.getResponse().readEntity(String.class), LoggingLevel.ERROR);
        }
        catch (Exception e) {
            logService.publish(String.format("Notify New Service Registered Event Failed! Cause: %s", e.getMessage()), LoggingLevel.ERROR);
        }

    }

    private void setServiceInteractionValues() {
        if (apiToken == null) {
            apiToken = config.getOptionalValue("keymaster_service.api.token", String.class)
                    .orElseThrow(() -> new IllegalStateException("keymaster_service API token not present!"));
        }
        if (serviceName == null) {
            serviceName =  config.getOptionalValue("keymaster_service.service.name", String.class)
                    .orElseThrow(() -> new IllegalStateException("keymaster_service service name not present!"));
        }
    }
}
