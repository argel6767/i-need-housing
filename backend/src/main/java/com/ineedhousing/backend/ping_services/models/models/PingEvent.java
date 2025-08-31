package com.ineedhousing.backend.ping_services.models.models;

import java.time.LocalDateTime;

public record PingEvent(String message, LocalDateTime timeStamp) {
}
