package com.ineedhousing.models.log;

import java.util.Collection;

public record LogsWrapper(Collection<LogEvent> logs) {
}
