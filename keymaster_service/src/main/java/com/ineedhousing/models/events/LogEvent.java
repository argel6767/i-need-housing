package com.ineedhousing.models.events;

import java.time.LocalDateTime;

public record LogEvent(String log, String level, String service, LocalDateTime timeStamp) {
}
