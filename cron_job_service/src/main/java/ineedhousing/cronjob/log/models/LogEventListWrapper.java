package ineedhousing.cronjob.log.models;

import java.util.Collection;

public record LogEventListWrapper(Collection<LogEvent> logs) {
}
