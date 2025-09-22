package com.ineedhousing.backend.ws.v2;

import com.ineedhousing.backend.constants.Service;
import com.ineedhousing.backend.ws.v1.ServiceLogStreamManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LogStreamWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ServiceLogStreamManager serviceLogStreamManager;

    public LogStreamWebSocketHandler(ServiceLogStreamManager serviceLogStreamManager) {
        this.serviceLogStreamManager = serviceLogStreamManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws URISyntaxException {
        sessions.add(session);
        Service service = getService(session);
        serviceLogStreamManager.startLogStream(service);
    }

    private Service getService(WebSocketSession session) {
        String uri = String.valueOf(session.getUri());

        return switch (uri) {
            case "/cron_jobs/logs" -> Service.CRON_JOB_SERVICE;
            case "/keymaster/logs" -> Service.KEYMASTER_SERVICE;
            case "/email_service/logs" -> Service.EMAIL_SERVICE;
            case "/new_listings/logs" -> Service.NEW_LISTINGS_SERVICE;
            default -> throw new IllegalStateException("Unexpected value: " + uri);
        };
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        if (sessions.isEmpty()) {
            serviceLogStreamManager.stopLogStream();
        }
    }

    public void broadcastLog(String logLine) {
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(logLine));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
