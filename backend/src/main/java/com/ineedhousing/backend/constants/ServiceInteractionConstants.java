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

    public List<String> getServiceUrls() {
        return List.of(cronJobServiceUrl, keymasterServiceUrl, newListingsServiceUrl, emailServiceUrl);
    }

}
