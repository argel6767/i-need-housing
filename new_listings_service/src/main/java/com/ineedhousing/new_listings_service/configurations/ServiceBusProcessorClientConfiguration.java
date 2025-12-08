package com.ineedhousing.new_listings_service.configurations;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusErrorSource;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.new_listings_service.models.events.ServiceCollectionEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.AirbnbCollectionEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.RentCastCollectionEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.ZillowCollectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ServiceBusProcessorClientConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger =
            LoggerFactory.getLogger(ServiceBusProcessorClientConfiguration.class);

    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    private final ServiceBusClientBuilder clientBuilder;

    public ServiceBusProcessorClientConfiguration(
            ApplicationEventPublisher eventPublisher,
            ObjectMapper objectMapper,
            ServiceBusClientBuilder clientBuilder) {
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
        this.clientBuilder = clientBuilder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("ApplicationReadyEvent received – starting Service Bus listener...");

        ServiceBusProcessorClient processorClient =
                clientBuilder
                        .processor()
                        .queueName("your-queue-name")
                        .processMessage(this::handleMessage)
                        .processError(this::handleError)
                        .buildProcessorClient();

        processorClient.start();

        logger.info("Service Bus listener started successfully.");
    }

    private void handleMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        try {
            ServiceCollectionEvent event =
                    objectMapper.readValue(message.getBody().toString(), ServiceCollectionEvent.class);

            logger.info("Received ServiceCollectionEvent for service '{}'", event.serviceName());

            switch (event.serviceName()) {
                case Zillow -> eventPublisher.publishEvent(new ZillowCollectionEvent());
                case RentCast -> eventPublisher.publishEvent(new RentCastCollectionEvent());
                case Airbnb -> eventPublisher.publishEvent(new AirbnbCollectionEvent());
            }
        } catch (IOException ex) {
            logger.error("Failed to parse ServiceCollectionEvent: {}", ex.getMessage());
        }
    }

    private void handleError(ServiceBusErrorContext context) {
        if (context.getErrorSource() == ServiceBusErrorSource.RECEIVE) {
            logger.error(
                    "Error receiving messages from '{}': {}",
                    context.getEntityPath(),
                    context.getException().getMessage());
        } else {
            logger.error("Service Bus error: {}", context.getException().getMessage());
        }
    }
}