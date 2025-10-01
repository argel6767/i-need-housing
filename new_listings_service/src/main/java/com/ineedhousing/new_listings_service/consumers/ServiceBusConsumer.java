package com.ineedhousing.new_listings_service.consumers;


import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.new_listings_service.models.responses.CityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusConsumer {
    Logger logger = LoggerFactory.getLogger(ServiceBusConsumer.class);
    private final ObjectMapper objectMapper;
    private final String queueName = "new-listings-job-queue";

    public ServiceBusConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ServiceBusListener(destination = queueName)
    public void processMessage(String jsonBody) {
        try {
            logger.info("Received message from queue: {}", jsonBody);
            CityDto city = objectMapper.readValue(jsonBody, CityDto.class);
            logger.info("Successfully transformed queue message to object {}", city);
        } catch (Exception e) {
            logger.error("Failed to transform queue message to object {}. Error message: {}", jsonBody, e.getMessage());
        }
    }
}
