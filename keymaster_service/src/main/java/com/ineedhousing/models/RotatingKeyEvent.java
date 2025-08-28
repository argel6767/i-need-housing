package com.ineedhousing.models;

import java.time.LocalDateTime;

public record RotatingKeyEvent(String message, LocalDateTime timeStamp) {
}
