package ineedhousing.cronjob.auth;

import ineedhousing.cronjob.configs.ServiceInteractionConfiguration;
import ineedhousing.cronjob.keymaster.KeymasterServiceRestClient;
import ineedhousing.cronjob.keymaster.models.ServiceVerificationDto;
import ineedhousing.cronjob.keymaster.models.VerifiedServiceDto;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;

@ApplicationScoped
public class ApiTokenValidationService {

    @Inject
    LogService logService;

    @Inject
    @RestClient
    KeymasterServiceRestClient keymasterServiceRestClient;

    @Inject
    ServiceInteractionConfiguration serviceInteractionConfiguration;

    public boolean isServiceAuthenticated(String token, String serviceName) {
        try {
            logService.publish("Verifying " + serviceName, LoggingLevel.INFO);
            String apiToken = serviceInteractionConfiguration.getApiToken();
            String cronJobServiceName = serviceInteractionConfiguration.getServiceName();
            VerifiedServiceDto verifiedServiceDto = keymasterServiceRestClient.verifyServiceRequest(apiToken, cronJobServiceName, new ServiceVerificationDto(token, serviceName, LocalDateTime.now()));
            return verifiedServiceDto.authorizedStatus().equals("Service is authorized");
        }
        catch (Exception e) {
            logService.publish("Failed to verify request API token and Service Name", LoggingLevel.ERROR);
            return false;
        }
    }
}
