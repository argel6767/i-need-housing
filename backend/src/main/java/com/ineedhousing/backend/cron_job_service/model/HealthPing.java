package com.ineedhousing.backend.cron_job_service.model;

import java.util.List;
import java.util.Map;

public record HealthPing(String message, List<HealthCheck> healthCheckResponses) {

    public record HealthCheck(String name, String status, Map<String, String> data){}
}
