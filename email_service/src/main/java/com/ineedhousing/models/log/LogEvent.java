package com.ineedhousing.models.log;

import java.time.LocalDateTime;

public record LogEvent(String log, String level, String service, LocalDateTime timeStamp) {
}
