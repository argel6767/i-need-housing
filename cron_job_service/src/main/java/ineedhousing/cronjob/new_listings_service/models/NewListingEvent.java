package ineedhousing.cronjob.new_listings_service.models;

import java.time.LocalDateTime;

public record NewListingEvent(String message, LocalDateTime timestamp) {
}
