package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.requests.ServiceVerificationDto;
import com.ineedhousing.new_listings_service.models.responses.VerifiedServiceDto;
import com.ineedhousing.new_listings_service.repositories.RegisteredServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class ServiceAuthorizationService {

    private final RegisteredServiceRepository registeredServiceRepository;
    private final Logger logger = LoggerFactory.getLogger(ServiceAuthorizationService.class);
    private final RestClient restClient;

    public ServiceAuthorizationService(RegisteredServiceRepository registeredServiceRepository, @Qualifier("keymaster_service") RestClient restClient) {
        this.registeredServiceRepository = registeredServiceRepository;
        this.restClient = restClient;
    }

    private boolean isServicePresent(String serviceName) {
        return registeredServiceRepository.findByServiceName(serviceName).isPresent();
    }

    public boolean isApiTokenAndServiceNameValid(String apiToken, String serviceName) {
        if (!isServicePresent(serviceName)) {
            logger.warn("{} does not exist as a service", serviceName);
            return false;
        }

        ServiceVerificationDto serviceVerificationDto = new ServiceVerificationDto(apiToken, serviceName, LocalDateTime.now());
        try {
            VerifiedServiceDto verifiedServiceDto = restClient.post()
                    .uri("/v1/auth/token-validity")
                    .accept(MediaType.APPLICATION_JSON)
                    .body(serviceVerificationDto)
                    .retrieve()
                    .toEntity(VerifiedServiceDto.class)
                    .getBody();
            return verifiedServiceDto.authorizedStatus().equals("Service is authorized");
        }
        catch (Exception e) {
            logger.error("{} service verification failed", serviceName, e);
            return false;
        }
    }
}
