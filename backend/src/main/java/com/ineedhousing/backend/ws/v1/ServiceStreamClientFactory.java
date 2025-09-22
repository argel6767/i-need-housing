package com.ineedhousing.backend.ws.v1;

import com.ineedhousing.backend.constants.Service;
import com.ineedhousing.backend.constants.ServiceInteractionConstants;
import com.ineedhousing.backend.ws.LogStreamProcessor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class ServiceStreamClientFactory {

    private final ServiceInteractionConstants serviceInteractionConstants;
    private final LogStreamProcessor logStreamProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public ServiceStreamClientFactory(ServiceInteractionConstants serviceInteractionConstants, LogStreamProcessor logStreamProcessor, ApplicationEventPublisher eventPublisher) {
        this.serviceInteractionConstants = serviceInteractionConstants;
        this.logStreamProcessor = logStreamProcessor;
        this.eventPublisher = eventPublisher;
    }

    public ServiceLogStreamClient createClient(Service service) throws URISyntaxException {
        return new ServiceLogStreamClient(
                new URI(serviceInteractionConstants.getWebSocketEndpoint(service)),
                Map.of("X-Api-Token", serviceInteractionConstants.getApiToken(), "X-Service-Name", serviceInteractionConstants.getServiceName()),
                logStreamProcessor,
                eventPublisher
        );
    }
}
