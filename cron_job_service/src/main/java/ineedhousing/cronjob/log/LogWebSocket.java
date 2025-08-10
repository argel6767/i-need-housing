package ineedhousing.cronjob.log;

import ineedhousing.cronjob.log.model.LogEvent;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.quarkus.websockets.next.*;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@WebSocket(path = "/live-logs")
public class LogWebSocket {

    private WebSocketConnection currentConnection;

    @Inject
    LogService logService;

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
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
