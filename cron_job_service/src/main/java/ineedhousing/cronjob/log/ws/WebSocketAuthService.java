package ineedhousing.cronjob.log.ws;

import ineedhousing.cronjob.auth.ApiTokenValidationService;
import io.quarkus.websockets.next.HandshakeRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;

@ApplicationScoped
public class WebSocketAuthService {

    @Inject
    ApiTokenValidationService apiTokenValidationService;


    /**
     * Checks for access token in Access-Header token
     * @param handshakeRequest
     * @return
     */
    public boolean isAuthenticated(HandshakeRequest handshakeRequest) {
        String apiToken = handshakeRequest.header("X-Api-Token");
        String serviceName = handshakeRequest.header("X-Service-Name");
        if (ObjectUtils.allNull(apiToken, serviceName)) {
            return false;
        }
        return apiTokenValidationService.isServiceAuthenticated(apiToken, serviceName);
    }

}
