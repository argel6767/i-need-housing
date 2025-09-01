package com.ineedhousing.backend.keymaster_service.models.responses;

import java.time.LocalDateTime;

public record RegisteredServiceDto(String message, String apiToken, String serviceName, LocalDateTime timestamp) {
}