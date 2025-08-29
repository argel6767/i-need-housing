package com.ineedhousing.backend.registered_services.models;

import java.time.LocalDateTime;

public record VerifiedServiceDto(String authorizedStatus, LocalDateTime timeStamp) {
}

