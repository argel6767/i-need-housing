package com.ineedhousing.models.dtos;

import com.ineedhousing.models.events.LogEvent;

import java.util.Collection;

public record LogEventListWrapperDto(Collection<LogEvent> logs) {
}
