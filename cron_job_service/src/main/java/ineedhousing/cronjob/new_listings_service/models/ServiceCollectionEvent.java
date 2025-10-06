package ineedhousing.cronjob.new_listings_service.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceCollectionEvent(ThirdPartyServiceName serviceName, String message, LocalDateTime timestamp, UUID jobId) {
}
