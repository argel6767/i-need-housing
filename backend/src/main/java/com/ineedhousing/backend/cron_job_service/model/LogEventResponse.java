package com.ineedhousing.backend.cron_job_service.model;

import java.time.LocalDateTime;
import java.util.List;

public record LogEventResponse(List<LogEvent> logs){
    public record LogEvent(String log, String level, LocalDateTime timeStamp) {

    }
}

