package com.ineedhousing.models.events;

import java.time.LocalDateTime;

public record LogEvent(String log, String level, LocalDateTime timeStamp) {
}
