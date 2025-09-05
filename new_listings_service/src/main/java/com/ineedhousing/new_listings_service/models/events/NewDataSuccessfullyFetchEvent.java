package com.ineedhousing.new_listings_service.models.events;

import java.time.LocalDateTime;

public record NewDataSuccessfullyFetchEvent(String message, Integer newListingsCount, LocalDateTime timeStamp) {
}
