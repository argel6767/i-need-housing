package com.ineedhousing.backend.registered_services.models;

import java.time.LocalDateTime;

public record ServiceVerificationDto(String apiToken, String serviceName, LocalDateTime timeStamp) {

}
