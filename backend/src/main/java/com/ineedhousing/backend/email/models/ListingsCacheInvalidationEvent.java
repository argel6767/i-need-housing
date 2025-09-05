package com.ineedhousing.backend.email.models;

import java.time.LocalDateTime;

public record ListingsCacheInvalidationEvent(String source, LocalDateTime timeStamp) {
}
