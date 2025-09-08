package main.java.com.ineedhousing.services;

import ineedhousing.cronjob.configs.ServiceInteractionConfiguration;
import ineedhousing.cronjob.keymaster.models.RotatingKeyEvent;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class KeymasterWebhookService {

    @Inject
    @RestClient
    KeymasterServiceRestClient keymasterServiceRestClient;

    @Inject
    ServiceInteractionConfiguration serviceInteractionConfiguration;

    public void triggerRegistrationKeyRotation(RotatingKeyEvent rotatingKeyEvent) {
        try {
            String apiToken = serviceInteractionConfiguration.getApiToken();
            String serviceName = serviceInteractionConfiguration.getServiceName();
            String successMessage = keymasterServiceRestClient.rotateKey(apiToken, serviceName, rotatingKeyEvent);
            logService.publish(successMessage, LoggingLevel.INFO);
        }
        catch (Exception e) {
            logService.publish(e.getMessage(), LoggingLevel.ERROR);
        }
    }
}
