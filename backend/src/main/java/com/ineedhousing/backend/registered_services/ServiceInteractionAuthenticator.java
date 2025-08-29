package com.ineedhousing.backend.registered_services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceInteractionAuthenticator {

    private final RegisteredServiceRepository registeredServiceRepository;
    private final PasswordEncoder passwordEncoder;

    public ServiceInteractionAuthenticator(RegisteredServiceRepository registeredServiceRepository, RegisteredServiceRepository registeredServiceRepository1, @Qualifier("Argon") PasswordEncoder passwordEncoder) {
        this.registeredServiceRepository = registeredServiceRepository1;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isApiTokenAndServiceNameValid(String apiToken, String serviceName) {
        if (!isServicePresent(serviceName)) {
            log.warn("{} does not exist as a service", serviceName);
            return false;
        }
        RegisteredService service = registeredServiceRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new UsernameNotFoundException("Service not found"));
        return passwordEncoder.matches(apiToken, service.getApiTokenHash());
    }

    private boolean isServicePresent(String serviceName) {
        return registeredServiceRepository.findByServiceName(serviceName).isPresent();
    }
}
