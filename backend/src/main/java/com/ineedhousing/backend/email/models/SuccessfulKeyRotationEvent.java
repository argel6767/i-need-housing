package com.ineedhousing.backend.email.models;

import java.time.LocalDateTime;

public record SuccessfulKeyRotationEvent(String message, String newKey, LocalDateTime timeStamp) {
}
