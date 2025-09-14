package com.ineedhousing.backend.cron_job_service.ws.v1;

import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import com.ineedhousing.backend.cron_job_service.LogStreamProcessor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class CronJobStreamClientFactory {

    private final ServiceInteractionConstants serviceInteractionConstants;
    private final LogStreamProcessor logStreamProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public CronJobStreamClientFactory(ServiceInteractionConstants serviceInteractionConstants, LogStreamProcessor logStreamProcessor, ApplicationEventPublisher eventPublisher) {
        this.serviceInteractionConstants = serviceInteractionConstants;
        this.logStreamProcessor = logStreamProcessor;
        this.eventPublisher = eventPublisher;
    }

    public CronJobLogStreamClient createClient() throws URISyntaxException {
        return new CronJobLogStreamClient(
                new URI(serviceInteractionConstants.getWebSocketEndpoint()),
                Map.of("X-Api-Token", serviceInteractionConstants.getApiToken(), "X-Service-Name", serviceInteractionConstants.getServiceName()),
                logStreamProcessor,
                eventPublisher
        );
    }
}
