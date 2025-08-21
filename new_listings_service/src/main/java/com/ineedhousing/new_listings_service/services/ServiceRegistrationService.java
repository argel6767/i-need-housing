package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.RegisteredService;
import com.ineedhousing.new_listings_service.models.requests.ServiceRegistrationDto;
import com.ineedhousing.new_listings_service.models.responses.RegisteredServiceDto;
import com.ineedhousing.new_listings_service.repositories.RegisteredServiceRepository;
import com.ineedhousing.new_listings_service.utils.ApiTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ServiceRegistrationService {

    @Value("${register.endpoint.secret.key}")
    private String secretKey;
    private final RegisteredServiceRepository registeredServiceRepository;
    private final ApiTokenGenerator apiTokenGenerator;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(ServiceRegistrationService.class);

    public ServiceRegistrationService(RegisteredServiceRepository registeredServiceRepository, ApiTokenGenerator apiTokenGenerator, PasswordEncoder passwordEncoder) {
        this.registeredServiceRepository = registeredServiceRepository;
        this.apiTokenGenerator = apiTokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisteredServiceDto registerService(ServiceRegistrationDto serviceRegistrationDto) {
        if (!serviceRegistrationDto.registerSecretKey().equals(secretKey)) {
            throw new SecurityException("Incorrect or missing secret key");
        }
        if (isServicePresent(serviceRegistrationDto.serviceName())) {
            throw new SecurityException("Service already exists");
        }

        String apiToken = apiTokenGenerator.generateApiToken(serviceRegistrationDto.serviceName());
        String apiTokenHash = passwordEncoder.encode(apiToken);
        RegisteredService newRegisteredService = new  RegisteredService(serviceRegistrationDto.serviceName(), apiTokenHash);
        registeredServiceRepository.save(newRegisteredService);
        return new RegisteredServiceDto(apiToken, Instant.now());
    }

    private boolean isServicePresent(String serviceName) {
        return registeredServiceRepository.findByServiceName(serviceName).isPresent();
    }

    public boolean isApiTokenAndServiceNameValid(String apiToken, String serviceName) {
        if (!isServicePresent(serviceName)) {
            logger.warn("{} does not exist as a service", serviceName);
            return false;
        }
        RegisteredService service = registeredServiceRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new UsernameNotFoundException("Service not found"));
        return passwordEncoder.matches(apiToken, service.getApiTokenHash());
    }
}
