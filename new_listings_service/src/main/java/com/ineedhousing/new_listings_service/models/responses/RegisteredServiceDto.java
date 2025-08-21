package com.ineedhousing.new_listings_service.models.responses;

import java.time.Instant;

public record RegisteredServiceDto(String apiToken, Instant timestamp) {
}
