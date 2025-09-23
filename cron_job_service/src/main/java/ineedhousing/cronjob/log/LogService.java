package ineedhousing.cronjob.log;

import ineedhousing.cronjob.log.model.LogEvent;
import ineedhousing.cronjob.log.model.LogEventListWrapper;
import ineedhousing.cronjob.log.model.LoggingLevel;
import ineedhousing.cronjob.log.model.MostRecentLogsCircularBuffer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.enterprise.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;

@ApplicationScoped
public class LogService {

    @Inject
    Event<LogEvent> logEventPublisher;

    private final String SERVICE_NAME = "CRON_JOB_SERVICE";

    private final MostRecentLogsCircularBuffer<LogEvent> mostRecentLogsCircularBuffer = new MostRecentLogsCircularBuffer<>();

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

    public LogEventListWrapper getMostRecentLogs(Integer limit) {
        List<LogEvent> logs =  mostRecentLogsCircularBuffer.getMostRecentLogs(limit);
        return new LogEventListWrapper(logs);
    }

    public LogEventListWrapper getMostRecentLogsCircularBuffer() {
        ArrayDeque<LogEvent> logs =  mostRecentLogsCircularBuffer.getBuffer();
        return new LogEventListWrapper(logs);
    }
}
