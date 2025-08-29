package ineedhousing.cronjob.keymaster.models;

import java.time.LocalDateTime;

public record RotatingKeyEvent(String message, LocalDateTime timeStamp) {
}
