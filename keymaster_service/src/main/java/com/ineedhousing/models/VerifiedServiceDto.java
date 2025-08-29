package com.ineedhousing.models;

import java.time.LocalDateTime;

public record VerifiedServiceDto(String authorizedStatus, LocalDateTime timeStamp) {
}
