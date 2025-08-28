package com.ineedhousing.resources;

import com.ineedhousing.models.RotatingKeyEvent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/webhooks")
public class RotationKeyWebhookController {

    @Inject
    Event<RotatingKeyEvent> publisher;

    @POST
    @Path("/rotate-key")
    @Consumes(MediaType.APPLICATION_JSON)
    public String triggerKeyRotation(RotatingKeyEvent event) {
        publisher.fireAsync(event);
        return "Key rotation triggered";
    }
}
