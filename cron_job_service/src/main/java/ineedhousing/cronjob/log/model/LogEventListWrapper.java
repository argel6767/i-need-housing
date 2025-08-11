package ineedhousing.cronjob.log.model;

import java.util.Collection;

public record LogEventListWrapper(Collection<LogEvent> logs) {
}
