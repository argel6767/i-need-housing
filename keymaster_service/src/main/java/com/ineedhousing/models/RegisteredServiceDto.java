package com.ineedhousing.models;

import java.time.LocalDateTime;

public record RegisteredServiceDto(String message, String apiToken, String serviceName, LocalDateTime timestamp) {
}
