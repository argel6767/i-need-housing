package ineedhousing.cronjob.new_listings_service;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.azure.postgres.DatabaseService;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import ineedhousing.cronjob.new_listings_service.models.CityDto;
import ineedhousing.cronjob.new_listings_service.models.NewListingEvent;
import io.quarkus.runtime.LaunchMode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public void onSuccessfulListingsDeletion(@ObservesAsync NewListingEvent newListingEvent) {
        try {
            List<CityDto> cities = databaseService.fetchAllCities();
            if (cities == null || cities.isEmpty()) {
                logService.publish("No cities found. Canceling job", LoggingLevel.ERROR);
                return;
            }

            List<ServiceBusMessage> messages = cities.stream()
                    .map(cityDto -> {
                        try {
                            return objectMapper.writeValueAsString(cityDto);
                        } catch (JsonProcessingException e) {
                            logService.publish(String.format("Failed to parse city %s to JSON String", cityDto.cityName()), LoggingLevel.ERROR);
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
            serviceBusSenderClient.sendMessages(messages);
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
