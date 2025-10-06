package ineedhousing.cronjob.new_listings_service.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public record NewListingsCollectionEvent(String message, LocalDateTime timestamp) {
}
