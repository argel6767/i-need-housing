package ineedhousing.cronjob.keymaster;

import ineedhousing.cronjob.keymaster.models.RotatingKeyEvent;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keymaster-service")
public interface KeymasterServiceRestClient {

    @POST
    @Path("/v1/webhooks/rotate-key")
    String rotateKey(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName,  RotatingKeyEvent rotatingKeyEvent);

    @POST
    @Path("/v1/auth/token-validity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String verifyServiceRequest(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName, String serviceVerificationDtoJson);
}
