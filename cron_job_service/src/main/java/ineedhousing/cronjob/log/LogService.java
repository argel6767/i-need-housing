package ineedhousing.cronjob.log;

import ineedhousing.cronjob.log.model.LogEvent;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.enterprise.event.Event;

import java.time.LocalDateTime;

@ApplicationScoped
public class LogService {

    @Inject
    Event<LogEvent> logEventPublisher;

    public void publish(String message, LoggingLevel level) {
        switch (level) {
            case LoggingLevel.INFO -> Log.info(message);
            case LoggingLevel.DEBUG -> Log.debug(message);
            case LoggingLevel.TRACE -> Log.trace(message);
            case LoggingLevel.WARN -> Log.warn(message);
            case LoggingLevel.ERROR -> Log.error(message);
        }
        logEventPublisher.fireAsync(new LogEvent(message, level.toString(), LocalDateTime.now()));

    }
}
