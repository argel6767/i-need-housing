package com.ineedhousing.models.dtos;

import java.time.LocalDateTime;

public record RegistrationKeyDto(String key, LocalDateTime timeStamp) {
}
