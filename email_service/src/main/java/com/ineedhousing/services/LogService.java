package com.ineedhousing.services;

import com.ineedhousing.models.log.LogEvent;
import com.ineedhousing.models.log.LoggingLevel;
import com.ineedhousing.models.log.LogsWrapper;
import com.ineedhousing.utils.CircularBuffer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;

@ApplicationScoped
public class LogService {

    @Inject
    Event<LogEvent> logEventPublisher;

    private final String SERVICE_NAME = "EMAIL_SERVICE";

    private final CircularBuffer<LogEvent> mostRecentLogsCircularBuffer = new CircularBuffer<>();

    public void publish(String message, LoggingLevel level) {
        switch (level) {
            case LoggingLevel.INFO -> Log.info(message);
            case LoggingLevel.DEBUG -> Log.debug(message);
            case LoggingLevel.TRACE -> Log.trace(message);
            case LoggingLevel.WARN -> Log.warn(message);
            case LoggingLevel.ERROR -> Log.error(message);
        }
        LogEvent logEvent = new LogEvent(message, level.toString(), SERVICE_NAME, LocalDateTime.now());
        mostRecentLogsCircularBuffer.add(logEvent);
        logEventPublisher.fireAsync(logEvent);
    }

    public void info(String message) {
        publish(message, LoggingLevel.INFO);
    }

    public void debug(String message) {
        publish(message, LoggingLevel.DEBUG);
    }

    public void trace(String message) {
        publish(message, LoggingLevel.TRACE);
    }

    public void warn(String message) {
        publish(message, LoggingLevel.WARN);
    }

    public void error(String message) {
        publish(message, LoggingLevel.ERROR);
    }

    public LogsWrapper getMostRecentLogs(Integer limit) {
        List<LogEvent> logs =  mostRecentLogsCircularBuffer.getMostRecentLogs(limit);
        return new LogsWrapper(logs);
    }

    public LogsWrapper getMostRecentLogsCircularBuffer() {
        ArrayDeque<LogEvent> logs =  mostRecentLogsCircularBuffer.getBuffer();
        return new LogsWrapper(logs);
    }
}
