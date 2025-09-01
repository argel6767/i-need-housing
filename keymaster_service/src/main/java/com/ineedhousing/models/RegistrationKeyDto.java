package com.ineedhousing.models;

import java.time.LocalDateTime;

public record RegistrationKeyDto(String key, LocalDateTime timeStamp) {
}
