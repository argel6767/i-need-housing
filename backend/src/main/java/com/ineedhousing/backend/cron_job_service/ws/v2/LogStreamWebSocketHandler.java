package com.ineedhousing.backend.cron_job_service.ws.v2;

import com.ineedhousing.backend.cron_job_service.ws.v1.CronJobLogStreamManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class LogStreamWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final CronJobLogStreamManager cronJobLogStreamManager;

    public LogStreamWebSocketHandler(CronJobLogStreamManager cronJobLogStreamManager) {
        this.cronJobLogStreamManager = cronJobLogStreamManager;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws URISyntaxException {
        sessions.add(session);
        cronJobLogStreamManager.startLogStream();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        if (sessions.isEmpty()) {
            cronJobLogStreamManager.stopLogStream();
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
