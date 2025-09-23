package com.ineedhousing.backend.ws.v1;

import com.ineedhousing.backend.cron_job_service.models.LogEventResponse;
import com.ineedhousing.backend.model.CircularBuffer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceLogStreamService {

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
