package com.ineedhousing.backend.keymaster_service;

import com.ineedhousing.backend.exception.exceptions.ServiceUnavailableException;
import com.ineedhousing.backend.keymaster_service.models.requests.ServiceRegistrationDto;
import com.ineedhousing.backend.keymaster_service.models.responses.RegisteredServiceDto;
import com.ineedhousing.backend.keymaster_service.models.responses.RegistrationKeyDto;
import com.ineedhousing.backend.model.FailedServiceInteractionDto;
import com.ineedhousing.backend.ping_services.models.models.PingAllServicesEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingKeymasterServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingNewListingsServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

@Service
@Slf4j
public class KeymasterRestService {

    private final RestClient restClient;

    public KeymasterRestService(@Qualifier("keymaster_service") RestClient restClient) {
        this.restClient = restClient;
    }

    public void pingService() {
        try {
            String response = restClient.post()
                    .uri("/ping")
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);
            log.info(response);
        }
        catch (RestClientException e) {
            log.warn("Service was pinged but overall request was not successful: {},", e.getMessage());
        }
        catch (Exception e) {
            log.error("Service was unsuccessful: {},", e.getMessage());
        }
    }

    @EventListener
    @Async
    public void onPingAllServicesEvent(PingAllServicesEvent pingAllServicesEvent) {
        log.info("Pinging Keymaster Service during all services pinged event");
        pingService();
    }

    @EventListener
    @Async
    public void onPingServiceEvent(PingKeymasterServiceEvent pingServiceEvent) {
        log.info("Pinging Keymaster Service");
        pingService();
    }

    public RegisteredServiceDto registerNewService(String serviceName) {
        String key = fetchRegistrationKey();
        ServiceRegistrationDto serviceRegistrationDto = new ServiceRegistrationDto(serviceName, key);
        try {
            RegisteredServiceDto newService = restClient.post()
                    .uri("/v1/auth/register")
                    .body(serviceRegistrationDto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(RegisteredServiceDto.class);
            if (newService == null) {
                log.error("Service was unsuccessfully registered by Keymaster Service");
                throw new ServiceUnavailableException("Service was not successfully registered", new FailedServiceInteractionDto("Service was not successfully registered", LocalDateTime.now(), null));
            }
            return newService;
        }
        catch (RestClientException e) {
            log.error("Service was unsuccessfully registered by Keymaster Service: {},", e.getMessage());
            throw new ServiceUnavailableException(String.format("Service was not successfully registered by Keymaster Service. Message: %s ", e.getMessage()), new FailedServiceInteractionDto(e.getMessage(), LocalDateTime.now(), e.getCause().toString()));
        }
    }

    private String fetchRegistrationKey() {
        log.info("Fetching Registration Key");
        try {
            RegistrationKeyDto registrationKeyDto = restClient.get()
                    .uri("/v1/registration-key")
                    .retrieve()
                    .body(RegistrationKeyDto.class);
            if (registrationKeyDto == null) {
                log.error("Keymaster Service request was unsuccessful in fetching Registration Key");
                throw new ServiceUnavailableException("Fetching Registration Key from Keymaster Service was unsuccessful", new FailedServiceInteractionDto("Fetching Registration Key from Keymaster Service was unsuccessful", LocalDateTime.now(), null));
            }
            log.info("Registration Key fetched. Timestamp {},", registrationKeyDto.timeStamp());
            log.info(registrationKeyDto.toString());
            return registrationKeyDto.key();
        }
        catch (RestClientException e) {
            log.error("Keymaster Service request was unsuccessful in fetching Registration Key: {},", e.getMessage());
            throw new ServiceUnavailableException("Fetching Registration Key from Keymaster Service was unsuccessful. Response: " + e.getMessage(), new FailedServiceInteractionDto(e.getMessage(), LocalDateTime.now(), e.getCause().toString()));
        }

    }
}
