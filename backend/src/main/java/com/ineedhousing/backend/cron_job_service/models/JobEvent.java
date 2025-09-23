package com.ineedhousing.backend.cron_job_service.models;

import java.time.LocalDateTime;

public record JobEvent(Long id, String job, JobStatus status, LocalDateTime timestamp) {
}
