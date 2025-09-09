package com.ineedhousing.models.responses;

public record FailedRequestDto(String message, String timeStamp, String cause) {
}
