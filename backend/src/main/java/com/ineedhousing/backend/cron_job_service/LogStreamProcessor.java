package com.ineedhousing.backend.cron_job_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import org.springframework.stereotype.Service;

@Service
public class LogStreamProcessor {

    private final ObjectMapper mapper;

    public LogStreamProcessor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public LogEventResponse.LogEvent process(String rawLogStreamMessage) throws JsonProcessingException {
        return mapper.readValue(rawLogStreamMessage, LogEventResponse.LogEvent.class);
    }
}
