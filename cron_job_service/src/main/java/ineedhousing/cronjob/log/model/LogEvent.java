package ineedhousing.cronjob.log.model;

import java.time.LocalDateTime;

public record LogEvent(String log, String level, LocalDateTime timeStamp) {
}
