package ineedhousing.cronjob.log.models;

import java.time.LocalDateTime;


public record LogEvent(String log, String level, String service, LocalDateTime timeStamp) {
}
