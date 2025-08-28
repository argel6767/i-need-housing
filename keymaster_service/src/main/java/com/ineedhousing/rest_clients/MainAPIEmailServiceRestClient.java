package com.ineedhousing.rest_clients;

import com.ineedhousing.models.EmailDto;
import com.ineedhousing.models.SuccessfulKeyRotationEvent;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "api-email")
public interface MainAPIEmailServiceRestClient {

    @POST
    @Path("/emails/notifications/key-rotation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    EmailDto notifyNewKeyRotation(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName, SuccessfulKeyRotationEvent event);
}
