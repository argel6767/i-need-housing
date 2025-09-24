package ineedhousing.cronjob.log;

import ineedhousing.cronjob.log.models.LogEvent;
import ineedhousing.cronjob.log.models.LogEventListWrapper;
import ineedhousing.cronjob.log.models.LoggingLevel;
import ineedhousing.cronjob.log.models.CircularBuffer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.enterprise.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@ApplicationScoped
public class LogService {

    @Inject
    Event<LogEvent> logEventPublisher;

    private final String SERVICE_NAME = "CRON_JOB_SERVICE";

    private final CircularBuffer<LogEvent> circularBuffer = new CircularBuffer<>();

    public void publish(String message, LoggingLevel level) {
        switch (level) {
            case LoggingLevel.INFO -> Log.info(message);
            case LoggingLevel.DEBUG -> Log.debug(message);
            case LoggingLevel.TRACE -> Log.trace(message);
            case LoggingLevel.WARN -> Log.warn(message);
            case LoggingLevel.ERROR -> Log.error(message);
        }
        LogEvent logEvent = new LogEvent(message, level.toString(), SERVICE_NAME, LocalDateTime.now());
        circularBuffer.add(logEvent);
        logEventPublisher.fireAsync(logEvent);
    }

    public LogEventListWrapper getMostRecentLogs(Integer limit) {
        List<LogEvent> logs =  circularBuffer.getMostRecentEntries(limit);
        return new LogEventListWrapper(logs);
    }

    public LogEventListWrapper getMostRecentLogsCircularBuffer() {
        ConcurrentLinkedDeque<LogEvent> logs =  circularBuffer.getBuffer();
        return new LogEventListWrapper(logs);
    }
}
