package com.ineedhousing.backend.email.models;

import java.time.LocalDateTime;

public record NewDataSuccessfullyFetchedEvent(String source, String message, Integer newListingsCount, LocalDateTime timeStamp) {
}
