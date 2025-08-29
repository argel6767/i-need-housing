package ineedhousing.cronjob.keymaster.models;

import java.time.LocalDateTime;

public record ServiceVerificationDto(String apiToken, String serviceName, LocalDateTime timeStamp) {
}
