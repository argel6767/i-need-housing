package com.ineedhousing.new_listings_service.models.events;

import java.time.LocalDateTime;

public record NewListingsCityEvent(Long id, String cityName, Double latitude, Double longitude, LocalDateTime timestamp) {
}
