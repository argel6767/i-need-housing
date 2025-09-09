package com.ineedhousing.new_listings_service.models.events;

import java.time.LocalDateTime;

public record NewDataSuccessfullyFetchedEvent(String source, String message, Integer newListingsCount, LocalDateTime timeStamp) {
}
