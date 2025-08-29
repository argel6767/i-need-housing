package com.ineedhousing.new_listings_service.models.responses;

import java.time.LocalDateTime;

public record VerifiedServiceDto(String authorizedStatus, LocalDateTime timeStamp) {
}
