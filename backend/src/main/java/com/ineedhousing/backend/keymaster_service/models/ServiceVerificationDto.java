package com.ineedhousing.backend.keymaster_service.models;

import java.time.LocalDateTime;

public record ServiceVerificationDto(String apiToken, String serviceName, LocalDateTime timeStamp) {

}
