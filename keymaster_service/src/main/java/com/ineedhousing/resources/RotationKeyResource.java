package com.ineedhousing.resources;

import com.ineedhousing.models.RegistrationKeyDto;
import com.ineedhousing.models.RotatingKeyEvent;
import com.ineedhousing.services.RegistrationKeyRotator;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.temporal.ChronoUnit;

@Path("/v1")
public class RotationKeyResource {

    @Inject
    Event<RotatingKeyEvent> rotatingKeyEventPublisher;

    @Inject
    RegistrationKeyRotator registrationKeyRotator;

    @POST
    @Path("/webhooks/rotate-key")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 1, windowUnit = ChronoUnit.MINUTES)
    public String triggerKeyRotation(RotatingKeyEvent event) {
        rotatingKeyEventPublisher.fireAsync(event);
        return "Key rotation triggered";
    }

    @GET
    @Path("/registration-key")
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 1, windowUnit = ChronoUnit.MINUTES)
    public RegistrationKeyDto getRegistrationKey(@HeaderParam("X-Service-Name") String serviceName) {
        return registrationKeyRotator.getRegistrationKey(serviceName);
    }
}
