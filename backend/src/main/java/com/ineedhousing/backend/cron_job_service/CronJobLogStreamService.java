package com.ineedhousing.backend.cron_job_service;

import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.model.CircularBuffer;

import java.util.List;

public class CronJobLogStreamService {

    private final int MAX_LOGS_SAVED = 1000;
    private final CircularBuffer<LogEventResponse.LogEvent> buffer = new CircularBuffer<>(MAX_LOGS_SAVED);

    public void addLog(LogEventResponse.LogEvent log) {
        buffer.add(log);
    }

    public void clearSavedLogs() {
        buffer.clear();
    }

    public List<LogEventResponse.LogEvent> getLogs() {
        return buffer.getBuffer().stream().toList();
    }
}
