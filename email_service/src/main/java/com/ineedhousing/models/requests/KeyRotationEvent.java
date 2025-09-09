package com.ineedhousing.models.requests;

import java.time.LocalDateTime;

public record KeyRotationEvent(String message, String newKey, LocalDateTime timeStamp) {
}
