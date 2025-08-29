package ineedhousing.cronjob.configs;

import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;

@ApplicationScoped
public class ServiceInteractionConfiguration {
    @Inject
    Config config;

    private String apiToken;
    private String serviceName;

    @PostConstruct
    void init() {
        apiToken = config.getOptionalValue("service.api.token", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("new_listings_service API token not present!"));
        serviceName = config.getOptionalValue("service.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("new_listings_service service name not present!"));
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getServiceName() {
        return serviceName;
    }
}
