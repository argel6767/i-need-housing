package com.ineedhousing.backend.cron_job_service.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ineedhousing.backend.cron_job_service.LogStreamProcessor;
import com.ineedhousing.backend.cron_job_service.model.ClearSavedLogsEvent;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.model.PublishedParsedLog;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Log
public class CronJobLogStreamClient extends WebSocketClient {

    private final LogStreamProcessor logStreamProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public CronJobLogStreamClient(URI serviceUrl, Map<String, String> headers, LogStreamProcessor logStreamProcessor, ApplicationEventPublisher eventPublisher) throws URISyntaxException {
        super(serviceUrl, headers);
        this.logStreamProcessor = logStreamProcessor;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Connected to Cron Job Service live logs. Status Code: " + serverHandshake.getHttpStatus() +  " & Message: " +  serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String logLine) {
        try {
            LogEventResponse.LogEvent log = logStreamProcessor.process(logLine);
            eventPublisher.publishEvent(new PublishedParsedLog(log));
        } catch (JsonProcessingException e) {
            log.warning("Failed to process logLine: " + logLine);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Disconnected from Cron Job Service live logs. Reason: " + s);
        eventPublisher.publishEvent(new ClearSavedLogsEvent());
    }

    @Override
    public void onError(Exception e) {
        log.warning(e.getMessage());
    }
}
