package com.ineedhousing.backend.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public final class ServiceInteractionConstants {

    @Value("${cron.job.service.url}")
    private String cronJobServiceUrl;

    @Value("${cron.job.service.web.socket.endpoint}")
    private String webSocketEndpoint;

    @Value("${key.master.service.url}")
    private String keymasterServiceUrl;

    @Value("${new.listings.service.url}")
    private String newListingsServiceUrl;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    @Value("${service.api.token}")
    private String apiToken;

    @Value("${service.name}")
    private String serviceName;

    @Value("${spring.devtools.restart.enabled}")
    private boolean isDev;

    public List<String> getServiceUrls() {
        return List.of(cronJobServiceUrl, keymasterServiceUrl, newListingsServiceUrl, emailServiceUrl);
    }

    public String getWebSocketEndpoint(Service services) {
        String url = switch (services) {
            case CRON_JOB_SERVICE -> cronJobServiceUrl;
            case KEYMASTER_SERVICE -> keymasterServiceUrl;
            case EMAIL_SERVICE -> emailServiceUrl;
            case NEW_LISTINGS_SERVICE -> newListingsServiceUrl;
        };

        if (isDev) {
            return url.replaceFirst("^https?://", "wss://");
        } else {
            return url.replaceFirst("^http://", "ws://")
                    .replaceFirst("^https://", "wss://");
        }
    }

}
