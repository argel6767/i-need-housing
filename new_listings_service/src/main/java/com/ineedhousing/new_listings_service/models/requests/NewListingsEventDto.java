package com.ineedhousing.new_listings_service.models.requests;

import java.time.LocalDateTime;


public record NewListingsEventDto(String message, LocalDateTime timestamp) {
}
