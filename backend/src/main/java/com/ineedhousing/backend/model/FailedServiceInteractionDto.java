package com.ineedhousing.backend.model;

import java.time.LocalDateTime;

public record FailedServiceInteractionDto(String message, LocalDateTime timeStamp, String cause) {
}
