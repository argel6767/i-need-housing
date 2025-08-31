package com.ineedhousing.backend.registered_services;

import com.ineedhousing.backend.ping_services.models.models.PingEvent;
import com.ineedhousing.backend.registered_services.models.ServiceVerificationDto;
import com.ineedhousing.backend.registered_services.models.VerifiedServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ServiceInteractionAuthenticator {

    private final RegisteredServiceRepository registeredServiceRepository;
    private final RestClient restClient;

    public ServiceInteractionAuthenticator(RegisteredServiceRepository registeredServiceRepository, @Qualifier("keymaster_service") RestClient restClient) {
        this.registeredServiceRepository = registeredServiceRepository;
        this.restClient = restClient;
    }

    public boolean isApiTokenAndServiceNameValid(String apiToken, String serviceName) {
        if (!isServicePresent(serviceName)) {
            log.warn("{} does not exist as a service", serviceName);
            return false;
        }

        ServiceVerificationDto serviceVerificationDto = new ServiceVerificationDto(apiToken, serviceName, LocalDateTime.now());
       try {
           log.info("Checking if service {} is authorized", serviceName);
           VerifiedServiceDto dto = restClient.post()
               .uri("/v1/auth/token-validity")
               .body(serviceVerificationDto)
               .accept(MediaType.APPLICATION_JSON)
               .retrieve()
               .toEntity(VerifiedServiceDto.class)
               .getBody();
           if (dto == null) {
               log.warn("Keymaster service call failed to return authorized status of {}", serviceName);
               return false;
           }
           log.info("Successfully fetched status of {}, verifying if {} is authorized", serviceName, serviceName);
           return dto.authorizedStatus().equals("Service is authorized");
       }
       catch (Exception e) {
           log.error("Failed to verify service {}, Type of failure: {}, message: {}", serviceName, e.getClass(), e.getMessage());
           return false;
       }
    }

    @EventListener
    @Async
    public void pingService(PingEvent pingEvent) {
        log.info("Pinging New Listings Service");
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

    private boolean isServicePresent(String serviceName) {
        return registeredServiceRepository.findByServiceName(serviceName).isPresent();
    }
}
