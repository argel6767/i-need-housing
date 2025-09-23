package com.ineedhousing.backend.ws.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.model.PublishedParsedLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogBroadcasterBridge {

    private final LogStreamWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public LogBroadcasterBridge(LogStreamWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void onLogReceived(PublishedParsedLog parsedLog) {
        LogEventResponse.LogEvent event = parsedLog.log();

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        }
        catch (Exception e) {
            log.error("Error serializing LogEventResponse, falling back to plain toString. Error message: {}", e.getMessage());
            payload = event.toString();
        }

        webSocketHandler.broadcastLog(event.service(), payload);
    }
}