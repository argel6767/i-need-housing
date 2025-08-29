package com.ineedhousing.new_listings_service.models.requests;

import java.time.LocalDateTime;

public record ServiceVerificationDto(String apiToken, String serviceName, LocalDateTime timeStamp) {

}