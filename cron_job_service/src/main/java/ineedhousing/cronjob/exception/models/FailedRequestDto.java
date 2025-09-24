package ineedhousing.cronjob.exception.models;

public record FailedRequestDto(String message, String timeStamp, String cause) {
}
