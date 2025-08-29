package com.ineedhousing.resources;

import com.ineedhousing.models.RegisteredServiceDto;
import com.ineedhousing.models.RegistrationDto;
import com.ineedhousing.models.ServiceVerificationDto;
import com.ineedhousing.models.VerifiedServiceDto;
import com.ineedhousing.services.ApiTokenValidationService;
import com.ineedhousing.services.ServiceAuthenticatorService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.temporal.ChronoUnit;

@Path("/v1/auth")
public class ServiceAuthenticatorResource {

    @Inject
    ServiceAuthenticatorService authenticatorService;

    @Inject
    ApiTokenValidationService apiTokenValidationService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10, windowUnit = ChronoUnit.MINUTES)
    public RegisteredServiceDto registerService(RegistrationDto registrationDto) {
        return authenticatorService.registerService(registrationDto);
    }

    @POST
    @Path("/token-validity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10, windowUnit = ChronoUnit.MINUTES)
    public VerifiedServiceDto verifyServiceToken(ServiceVerificationDto serviceVerificationDto) {
        return apiTokenValidationService.isServiceAuthenticated(serviceVerificationDto);
    }
}
