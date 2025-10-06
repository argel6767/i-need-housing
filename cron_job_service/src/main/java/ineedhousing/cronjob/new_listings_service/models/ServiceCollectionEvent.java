package ineedhousing.cronjob.new_listings_service.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;
import java.util.UUID;

@RegisterForReflection
public record ServiceCollectionEvent(ThirdPartyServiceName serviceName, String message, LocalDateTime timestamp, UUID jobId) {
}
