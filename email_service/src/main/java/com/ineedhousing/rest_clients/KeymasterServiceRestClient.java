package com.ineedhousing.rest_clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keymaster-service")
public interface KeymasterServiceRestClient {

    @POST
    @Path("/v1/auth/token-validity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String verifyServiceRequest(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName, String serviceVerificationDtoJson);
}
