package com.ineedhousing.models;

import java.time.LocalDateTime;

public record EmailDto(String confirmationMessage, LocalDateTime timeStamp) {
}
