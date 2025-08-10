package com.ineedhousing.backend.cron_job_service.model;

import java.util.List;

public record HealthPing(String message, List<Object> healthChecks) {
}
