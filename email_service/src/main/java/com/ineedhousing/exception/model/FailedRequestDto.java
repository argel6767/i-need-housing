package com.ineedhousing.exception.model;

public record FailedRequestDto(String message, String timeStamp, String cause) {
}
