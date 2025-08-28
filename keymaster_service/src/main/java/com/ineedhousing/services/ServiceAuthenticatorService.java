package com.ineedhousing.services;

import com.ineedhousing.models.RegisteredServiceDto;
import com.ineedhousing.models.RegistrationDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServiceAuthenticatorService {

    @Inject
    RegistrationKeyRotator rotator;

    public RegisteredServiceDto registerService(RegistrationDto registrationDto) {
        String registrationKey = rotator.getKey();
        if (registrationKey == null) {
            throw new IllegalStateException("Registration key has not been initialized");
        }
        
    }
}
