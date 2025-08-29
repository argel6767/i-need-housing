package com.ineedhousing.backend.registered_services;

import com.ineedhousing.backend.registered_services.models.ServiceVerificationDto;
import com.ineedhousing.backend.registered_services.models.VerifiedServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ServiceInteractionAuthenticator {

    private final RegisteredServiceRepository registeredServiceRepository;
    private final RestClient restClient;

    public ServiceInteractionAuthenticator(RegisteredServiceRepository registeredServiceRepository, RegisteredServiceRepository registeredServiceRepository1, @Qualifier("keymaster_service") RestClient restClient) {
        this.registeredServiceRepository = registeredServiceRepository1;
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
               .body(serviceVerificationDto)
               .accept(MediaType.APPLICATION_JSON)
               .retrieve()
               .toEntity(VerifiedServiceDto.class)
               .getBody();
           return dto.authorizedStatus().equals("Service is authorized");
       }
       catch (Exception e) {
           log.error("Failed to verify service {}", serviceName, e);
           return false;
       }
    }

    private boolean isServicePresent(String serviceName) {
        return registeredServiceRepository.findByServiceName(serviceName).isPresent();
    }
}
