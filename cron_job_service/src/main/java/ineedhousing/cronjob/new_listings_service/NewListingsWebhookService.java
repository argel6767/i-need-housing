package ineedhousing.cronjob.new_listings_service;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.azure.postgres.DatabaseService;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import ineedhousing.cronjob.new_listings_service.models.NewListingsCollectionEvent;
import ineedhousing.cronjob.new_listings_service.models.ServiceCollectionEvent;
import ineedhousing.cronjob.new_listings_service.models.ThirdPartyServiceName;
import io.quarkus.runtime.LaunchMode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NewListingsWebhookService {

    private static final Logger log = LoggerFactory.getLogger(NewListingsWebhookService.class);
    @Inject
    @RestClient
    NewListingsServiceRestClient  newListingsServiceRestClient;

    @Inject
    LogService logService;

    @Inject
    DatabaseService  databaseService;

    @Inject
    ServiceBusSenderClient serviceBusSenderClient;

    @Inject
    ServiceBusAdministrationClient serviceBusAdministrationClient;

    @Inject
    ObjectMapper objectMapper;

    private final String queueName = "new-listings-job-queue";

    public void onSuccessfulListingsDeletion(@ObservesAsync NewListingsCollectionEvent newListingsCollectionEvent) {
        try {
            List<ThirdPartyServiceName> services = ThirdPartyServiceName.all();

            if (services.isEmpty()) {
                logService.publish("No services found. Canceling job", LoggingLevel.ERROR);
                return;
            }

            List<ServiceBusMessage> serviceJobMessages = services.stream()
                    .map(serviceName -> new ServiceCollectionEvent(serviceName, newListingsCollectionEvent.message(), LocalDateTime.now(), UUID.randomUUID()))
                    .map(serviceCollection -> {
                        try {
                            return objectMapper.writeValueAsString(serviceCollection);
                        }
                        catch (JsonProcessingException e) {
                            logService.publish(String.format("Error converting service collection to JSON: %s. Error message: %s", serviceCollection.serviceName(), e.getMessage()), LoggingLevel.ERROR);
                            return "";
                        }
                    })
                    .filter(str -> !str.isEmpty())
                    .map(ServiceBusMessage::new)
                    .toList();

            if (!isQueuePresent()) {
                logService.publish(String.format("Queue does not exist. Making Queue: %s now", queueName), LoggingLevel.WARN);
                serviceBusAdministrationClient.createQueue(queueName);
            }

            logService.publish("Sending messages to Queue", LoggingLevel.INFO);
            serviceBusSenderClient.sendMessages(serviceJobMessages);
        } catch (Exception e) {
            logService.publish("Failed to trigger new listings job: " + e.getMessage(), LoggingLevel.ERROR);
        }
    }

    private boolean isQueuePresent() {
        if (LaunchMode.current().isDevOrTest()) {
            logService.publish("App currently in development mode, skipping check", LoggingLevel.INFO);
            return true;
        }
        return serviceBusAdministrationClient.getQueueExists(queueName);
    }
}
