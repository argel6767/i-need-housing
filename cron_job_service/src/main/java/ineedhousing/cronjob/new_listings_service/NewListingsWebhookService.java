package ineedhousing.cronjob.new_listings_service;

import ineedhousing.cronjob.configs.ServiceInteractionConfiguration;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import ineedhousing.cronjob.new_listings_service.models.NewListingEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class NewListingsWebhookService {

    @Inject
    @RestClient
    NewListingsServiceRestClient  newListingsServiceRestClient;

    @Inject
    LogService logService;

    @Inject
    ServiceInteractionConfiguration serviceInteractionConfiguration;

    public void onSuccessfulListingsDeletion(@ObservesAsync NewListingEvent newListingEvent) {
        try {
            String apiToken = serviceInteractionConfiguration.getApiToken();
            String serviceName = serviceInteractionConfiguration.getServiceName();
            String successMessage = newListingsServiceRestClient.newListingsWebhook(apiToken, serviceName, newListingEvent);
            logService.publish(successMessage, LoggingLevel.INFO);
        }
        catch (Exception e) {
            logService.publish("Failed to trigger new_listings_service webhook: " + e.getMessage(), LoggingLevel.ERROR);
        }
    }
}
