package com.ineedhousing.backend.model;

import java.time.LocalDateTime;

public record ServiceInteractionDto<T> (T serviceResponse, String message, LocalDateTime timeStamp){
}
