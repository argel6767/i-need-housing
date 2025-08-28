package com.ineedhousing.models;

import java.time.LocalDateTime;

public record SuccessfulKeyRotationEvent(String message, LocalDateTime timestamp) {
}
