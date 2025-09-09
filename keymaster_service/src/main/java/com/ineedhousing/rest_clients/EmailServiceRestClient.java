package com.ineedhousing.rest_clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "email-service")
public interface EmailServiceRestClient {

    @POST
    @Path("/v1/emails/services/key-rotation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 5, delay = 1000)
    String notifyNewKeyRotation(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName, String dtoJson);
}
