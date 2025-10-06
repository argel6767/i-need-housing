package com.ineedhousing.new_listings_service.configurations;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.spring.cloud.service.servicebus.consumer.ServiceBusErrorHandler;
import com.azure.spring.cloud.service.servicebus.consumer.ServiceBusRecordMessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.new_listings_service.models.events.ServiceCollectionEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.AirbnbCollectionEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.RentCastCollectionEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.ZillowCollectionEvent;
import com.ineedhousing.new_listings_service.services.ServiceAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration(proxyBeanMethods = false)
public class ServiceBusProcessorClientConfiguration {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(ServiceBusProcessorClientConfiguration.class);

    public ServiceBusProcessorClientConfiguration(ApplicationEventPublisher applicationEventPublisher, ObjectMapper objectMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.objectMapper = objectMapper;
    }

    @Bean
    ServiceBusRecordMessageListener processMessage() {
        return this::handleMessage;
    }

    private void handleMessage(ServiceBusReceivedMessageContext serviceBusReceivedMessageContext) {
        ServiceBusReceivedMessage message = serviceBusReceivedMessageContext.getMessage();
        try {
            ServiceCollectionEvent event = objectMapper.readValue(message.getBody().toString(), ServiceCollectionEvent.class);
            logger.info("Successfully received ServiceCollectionEvent: {}. Firing the appropriate event type", event);

            switch (event.serviceName()) {
                case Zillow -> applicationEventPublisher.publishEvent(new ZillowCollectionEvent());
                case RentCast -> applicationEventPublisher.publishEvent(new RentCastCollectionEvent());
                case Airbnb ->  applicationEventPublisher.publishEvent(new AirbnbCollectionEvent());
            }
        }
        catch (IOException ex) {
            logger.error("Failed to parse ServiceCollectionEvent: {}. Ending job", ex.getMessage());
        }
    }

    @Bean
    ServiceBusErrorHandler processError() {
        return context -> {
            logger.error("Error when receiving messages from namespace: '{}'. Entity: '{}", context.getFullyQualifiedNamespace(), context.getEntityPath());
        };
    }
}
