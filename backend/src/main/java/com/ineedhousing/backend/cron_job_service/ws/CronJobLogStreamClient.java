package com.ineedhousing.backend.cron_job_service.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ineedhousing.backend.cron_job_service.LogStreamProcessor;
import com.ineedhousing.backend.cron_job_service.model.ClearSavedLogsEvent;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.model.PublishedParsedLog;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Log
@Component
public class CronJobLogStreamClient extends WebSocketClient {

    private final LogStreamProcessor logStreamProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public CronJobLogStreamClient(@Value("${cron.job.service.url}") String serviceUrl, LogStreamProcessor logStreamProcessor, ApplicationEventPublisher eventPublisher) throws URISyntaxException {
        super(new URI(serviceUrl + "/live-logs"));
        this.logStreamProcessor = logStreamProcessor;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Connected to Cron Job Service live logs");
    }

    @Override
    public void onMessage(String logLine) {
        try {
            LogEventResponse.LogEvent log = logStreamProcessor.process(logLine);
            eventPublisher.publishEvent(new PublishedParsedLog(log));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Disconnected from Cron Job Service live logs");
        eventPublisher.publishEvent(new ClearSavedLogsEvent());
    }

    @Override
    public void onError(Exception e) {
        log.warning(e.getMessage());
        throw new RuntimeException(e);
    }
}
