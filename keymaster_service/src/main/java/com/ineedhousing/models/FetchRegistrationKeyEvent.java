package com.ineedhousing.models;

import java.time.LocalDateTime;

public record FetchRegistrationKeyEvent(String message, LocalDateTime timeStamp) {
}
