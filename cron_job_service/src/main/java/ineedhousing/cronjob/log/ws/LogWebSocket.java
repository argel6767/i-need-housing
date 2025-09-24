package ineedhousing.cronjob.log.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LogEvent;
import ineedhousing.cronjob.log.models.LoggingLevel;
import io.quarkus.websockets.next.*;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@WebSocket(path = "/live-logs")
public class LogWebSocket {

    private WebSocketConnection currentConnection;

    @Inject
    LogService logService;

    @Inject
    WebSocketAuthService authService;

    @Inject
    ObjectMapper objectMapper;

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        if (!authService.isAuthenticated(connection.handshakeRequest())) {
            logService.publish("Unauthenticated log connection request: " + connection, LoggingLevel.WARN);
            connection.closeAndAwait(new CloseReason(403, "Missing or incorrect access token"));
            throw new WebSocketException("Not authenticated, rejecting connection");
        }
        currentConnection = connection;
        logService.publish("Live log connection made", LoggingLevel.INFO);
    }

    @OnError
    public void onError(Throwable error) {
        currentConnection = null;
        logService.publish("Live log error: " + error.getMessage(), LoggingLevel.ERROR);
    }

    @OnClose
    public void onClose() {
        currentConnection = null;
        logService.publish("Live log closed", LoggingLevel.INFO);
    }

    public void onLogMessage(@ObservesAsync LogEvent log) {
        if (currentConnection != null) {
            try {
                String message = objectMapper.writeValueAsString(log);
                currentConnection.sendTextAndAwait(message);
            }
            catch (Exception e) {
                currentConnection = null;
            }
        }
    }
}
