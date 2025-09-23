package com.ineedhousing.backend.cron_job_service.models;

import com.ineedhousing.backend.constants.Service;

import java.time.LocalDateTime;
import java.util.List;

public record LogEventResponse(List<LogEvent> logs){
    public record LogEvent(String log, String level, Service service, LocalDateTime timeStamp) {

    }
}

