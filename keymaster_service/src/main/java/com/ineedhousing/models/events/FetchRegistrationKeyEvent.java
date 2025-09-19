package com.ineedhousing.models.events;

import java.time.LocalDateTime;

public record FetchRegistrationKeyEvent(String message, LocalDateTime timeStamp) {
}
