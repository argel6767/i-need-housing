package ineedhousing.cronjob.new_listings_service;

import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import ineedhousing.cronjob.new_listings_service.models.NewListingEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class NewListingsWebhookService {

    @Inject
    @RestClient
    NewListingsServiceRestClient  newListingsServiceRestClient;

    @Inject
    Config config;

    @Inject
    LogService logService;

    private String apiToken;
    private String serviceName;

    @PostConstruct
    public void init() {
        apiToken = config.getOptionalValue("new.listings.service.api.token", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("new_listings_service API token not present!"));
        serviceName =  config.getOptionalValue("new.listings.service.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("new_listings_service service name not present!"));
    }

    public void onSuccessfulListingsDeletion(@ObservesAsync NewListingEvent newListingEvent) {
        try {
            String successMessage = newListingsServiceRestClient.newListingsWebhook(apiToken, serviceName, newListingEvent);
            logService.publish(successMessage, LoggingLevel.INFO);
        }
        catch (Exception e) {
            logService.publish("Failed to trigger new_listings_service webhook: " + e.getMessage(), LoggingLevel.ERROR);
        }
    }
}
