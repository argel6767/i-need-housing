package com.ineedhousing.models.requests;

import java.time.LocalDateTime;

public record NewListingsMadeEvent(String source, String message, Integer newListingsCount, LocalDateTime timeStamp) {
}
