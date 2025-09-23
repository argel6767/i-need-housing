package com.ineedhousing.backend.ws.v2;

import com.ineedhousing.backend.constants.Service;
import com.ineedhousing.backend.ws.v1.ServiceLogStreamManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LogStreamWebSocketHandler extends TextWebSocketHandler {

    private final Map<Service, Set<WebSocketSession>> sessionsByService = new ConcurrentHashMap<>();
    private final ServiceLogStreamManager serviceLogStreamManager;

    public LogStreamWebSocketHandler(ServiceLogStreamManager serviceLogStreamManager) {
        this.serviceLogStreamManager = serviceLogStreamManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws URISyntaxException {
        Service service = getService(session);

        sessionsByService
                .computeIfAbsent(service, s -> ConcurrentHashMap.newKeySet())
                .add(session);

        serviceLogStreamManager.startLogStream(service);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Service service = getService(session);

        Set<WebSocketSession> set = sessionsByService.get(service);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) {
                serviceLogStreamManager.stopLogStream(service);
            }
        }
    }

    /** called by ServiceLogStreamClient when a line arrives */
    public void broadcastLog(Service service, String logLine) {
        Set<WebSocketSession> set = sessionsByService.get(service);
        if (set != null) {
            for (WebSocketSession s : set) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(logLine));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Service getService(WebSocketSession session) {
        return switch (session.getUri().getPath()) {
            case "/cron_job/logs"     -> Service.CRON_JOB_SERVICE;
            case "/keymaster/logs"    -> Service.KEYMASTER_SERVICE;
            case "/email_service/logs"-> Service.EMAIL_SERVICE;
            case "/new_listings/logs" -> Service.NEW_LISTINGS_SERVICE;
            default -> throw new IllegalStateException("Unexpected URI path: " + session.getUri().getPath());
        };
    }
}
