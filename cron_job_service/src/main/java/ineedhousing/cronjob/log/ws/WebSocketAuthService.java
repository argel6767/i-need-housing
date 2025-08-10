package ineedhousing.cronjob.log.ws;

import io.quarkus.websockets.next.HandshakeRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebSocketAuthService {

    private String accessToken;

    /**
     * Checks for access token in Access-Header token
     * @param handshakeRequest
     * @return
     */
    public boolean isAuthenticated(HandshakeRequest handshakeRequest) {
        String accessToken = getAccessToken();
        String requestToken = handshakeRequest.header("Access-Header");
        return accessToken.equals(requestToken);
    }

    private String getAccessToken() {
        if (accessToken == null) {
            accessToken = System.getenv("ACCESS_TOKEN_HEADER");
        }
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new RuntimeException("ACCESS_TOKEN_HEADER environment variable not found or empty!");
        }
        return accessToken;
    }
}
