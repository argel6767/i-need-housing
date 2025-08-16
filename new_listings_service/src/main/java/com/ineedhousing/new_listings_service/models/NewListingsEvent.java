package com.ineedhousing.new_listings_service.models;

import java.time.LocalDateTime;

public record NewListingsEvent(String message, LocalDateTime timestamp) {
}
