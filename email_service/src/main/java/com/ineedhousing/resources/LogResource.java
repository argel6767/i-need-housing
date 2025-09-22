package com.ineedhousing.resources;

import com.ineedhousing.models.log.LogEvent;
import com.ineedhousing.models.log.LoggingLevel;
import com.ineedhousing.services.LogService;
import com.ineedhousing.services.WebSocketAuthService;
import io.quarkus.websockets.next.*;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@WebSocket(path = "/live-logs")
public class LogResource {

    private WebSocketConnection currentConnection;

    @Inject
    LogService logService;

    @Inject
    WebSocketAuthService authService;

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
                currentConnection.sendTextAndAwait(log.toString());
            }
            catch (Exception e) {
                currentConnection = null;
            }
        }
    }
}
