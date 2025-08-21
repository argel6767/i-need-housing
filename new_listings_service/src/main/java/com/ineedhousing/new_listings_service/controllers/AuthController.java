package com.ineedhousing.new_listings_service.controllers;

import com.ineedhousing.new_listings_service.models.requests.ServiceRegistrationDto;
import com.ineedhousing.new_listings_service.models.responses.RegisteredServiceDto;
import com.ineedhousing.new_listings_service.services.ServiceRegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auths")
public class AuthController {

    private final ServiceRegistrationService serviceRegistrationService;

    public AuthController(ServiceRegistrationService serviceRegistrationService) {
        this.serviceRegistrationService = serviceRegistrationService;
    }

    @PostMapping("/register")
    public RegisteredServiceDto registerService(@RequestBody ServiceRegistrationDto serviceRegistrationDto) {
        return serviceRegistrationService.registerService(serviceRegistrationDto);
    }
}
