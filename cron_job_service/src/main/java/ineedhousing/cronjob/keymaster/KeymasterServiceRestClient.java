package ineedhousing.cronjob.keymaster;

import ineedhousing.cronjob.keymaster.models.RotatingKeyEvent;
import ineedhousing.cronjob.keymaster.models.ServiceVerificationDto;
import ineedhousing.cronjob.keymaster.models.VerifiedServiceDto;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keymaster-service")
public interface KeymasterServiceRestClient {

    @POST
    @Path("/v1/webhooks/rotate-key")
    String rotateKey(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName,  RotatingKeyEvent rotatingKeyEvent);

    @POST
    @Path("/v1/auth/token-validity")
    VerifiedServiceDto verifyServiceRequest(@HeaderParam("X-Api-Token") String apiToken, @HeaderParam("X-Service-Name") String serviceName, ServiceVerificationDto serviceVerificationDto);
}
