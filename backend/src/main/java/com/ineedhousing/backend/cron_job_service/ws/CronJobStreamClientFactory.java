package com.ineedhousing.backend.cron_job_service.ws;

import com.ineedhousing.backend.cron_job_service.LogStreamProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class CronJobStreamClientFactory {

    @Value("${cron.job.service.web.socket.endpoint}")
    private String serviceUrl;

    @Value("${cron.job.service.access.header}")
    private String accessToken;

    private final LogStreamProcessor logStreamProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public CronJobStreamClientFactory(LogStreamProcessor logStreamProcessor, ApplicationEventPublisher eventPublisher) {
        this.logStreamProcessor = logStreamProcessor;
        this.eventPublisher = eventPublisher;
    }

    public CronJobLogStreamClient createClient() throws URISyntaxException {
        return new CronJobLogStreamClient(
                new URI(serviceUrl),
                Map.of("Access-Header", accessToken),
                logStreamProcessor,
                eventPublisher
        );
    }
}
