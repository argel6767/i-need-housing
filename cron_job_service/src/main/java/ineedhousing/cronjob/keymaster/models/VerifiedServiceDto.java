package ineedhousing.cronjob.keymaster.models;

import java.time.LocalDateTime;

public record VerifiedServiceDto(String authorizedStatus, LocalDateTime timeStamp) {
}
