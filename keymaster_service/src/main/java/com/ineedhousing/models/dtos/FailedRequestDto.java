package com.ineedhousing.models.dtos;

public record FailedRequestDto(String message, String timeStamp, String cause) {
}
