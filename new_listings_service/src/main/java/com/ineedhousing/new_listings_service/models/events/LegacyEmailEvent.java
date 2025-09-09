package com.ineedhousing.new_listings_service.models.events;

import java.time.LocalDateTime;

public record LegacyEmailEvent(NewDataSuccessfullyFetchedEvent event, LocalDateTime timeStamp) {
}
