package com.ineedhousing.models;

public record FailedRequestDto(String message, String timeStamp, String cause) {
}
