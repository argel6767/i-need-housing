package com.ineedhousing.new_listings_service.services;

import com.ineedhousing.new_listings_service.models.RegisteredService;
import com.ineedhousing.new_listings_service.repositories.RegisteredServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceAuthorizationService {

    private final RegisteredServiceRepository registeredServiceRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(ServiceAuthorizationService.class);

    public ServiceAuthorizationService(RegisteredServiceRepository registeredServiceRepository, PasswordEncoder passwordEncoder) {
        this.registeredServiceRepository = registeredServiceRepository;
        this.passwordEncoder = passwordEncoder;
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
