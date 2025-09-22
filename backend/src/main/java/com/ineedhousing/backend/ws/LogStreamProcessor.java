package com.ineedhousing.backend.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Log
@Service
public class LogStreamProcessor {

    private final ObjectMapper mapper;
    private static final Pattern LOG_EVENT_PATTERN = Pattern.compile("LogEvent\\[log=(.+?), level=(.+?), timeStamp=(.+?)]");

    public LogStreamProcessor(@Qualifier("objectMapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public LogEventResponse.LogEvent process(String rawLogStreamMessage) throws JsonProcessingException {
        log.info("Attempting to map message: " + rawLogStreamMessage);
        try {
            return mapper.readValue(rawLogStreamMessage,LogEventResponse.LogEvent.class);
        }
        catch (JsonProcessingException e) {
            log.info("Failed to parse LogEvent via mapper, trying with Matching");
            return matchAttributes(rawLogStreamMessage);
        }
    }

    private LogEventResponse.LogEvent matchAttributes(String rawLogStreamMessage) {
        Matcher matcher = LOG_EVENT_PATTERN.matcher(rawLogStreamMessage);
        if (!matcher.matches()) {
            log.warning("Invalid LogEvent format" + rawLogStreamMessage);
            throw new IllegalArgumentException("Invalid LogEvent format: " + rawLogStreamMessage);
        }

        String logMessage = matcher.group(1);
        String level = matcher.group(2);
        String timeStampStr = matcher.group(3);

        try {
            LocalDateTime timeStamp = LocalDateTime.parse(timeStampStr);
            return new LogEventResponse.LogEvent(logMessage, level, timeStamp);
        } catch (Exception e) {
            log.warning("Failed to map timeStamp " + e);
            throw new IllegalArgumentException("Invalid timestamp format: " + timeStampStr, e);
        }
    }
}
