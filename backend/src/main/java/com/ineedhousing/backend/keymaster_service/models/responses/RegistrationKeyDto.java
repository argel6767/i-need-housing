package com.ineedhousing.backend.keymaster_service.models.responses;

import java.time.LocalDateTime;

public record RegistrationKeyDto(String key, LocalDateTime timeStamp) {
}
