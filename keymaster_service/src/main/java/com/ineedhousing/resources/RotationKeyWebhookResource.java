package com.ineedhousing.resources;

import com.ineedhousing.models.RotatingKeyEvent;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import java.time.temporal.ChronoUnit;

@Path("/v1/webhooks")
public class RotationKeyWebhookResource {

    @Inject
    Event<RotatingKeyEvent> publisher;

    @POST
    @Path("/rotate-key")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 1, windowUnit = ChronoUnit.MINUTES)
    public String triggerKeyRotation(RotatingKeyEvent event) {
        publisher.fireAsync(event);
        return "Key rotation triggered";
    }
}
