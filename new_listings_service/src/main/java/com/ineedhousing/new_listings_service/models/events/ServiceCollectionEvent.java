package com.ineedhousing.new_listings_service.models.events;

import com.ineedhousing.new_listings_service.models.data.ThirdPartyServiceName;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceCollectionEvent(ThirdPartyServiceName serviceName, String message, LocalDateTime timestamp, UUID jobId) {
}
