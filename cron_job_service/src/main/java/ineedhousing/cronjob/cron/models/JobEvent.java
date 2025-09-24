package ineedhousing.cronjob.cron.models;

import java.time.LocalDateTime;

public record JobEvent(Long id, String job, JobStatus status, LocalDateTime timestamp) {
}
