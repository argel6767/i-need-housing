package ineedhousing.cronjob.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import ineedhousing.cronjob.configs.ServiceInteractionConfiguration;
import ineedhousing.cronjob.keymaster.KeymasterServiceRestClient;
import ineedhousing.cronjob.keymaster.models.ServiceVerificationDto;
import ineedhousing.cronjob.keymaster.models.VerifiedServiceDto;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
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

    @Inject
    ObjectMapper objectMapper;

    public boolean isServiceAuthenticated(String token, String serviceName) {
        try {
            logService.publish("Verifying " + serviceName, LoggingLevel.INFO);
            String apiToken = serviceInteractionConfiguration.getApiToken();
            String cronJobServiceName = serviceInteractionConfiguration.getServiceName();


            logService.publish(String.format("Verifying incoming service: %s", serviceName), LoggingLevel.INFO);
            ServiceVerificationDto dto = new ServiceVerificationDto(token, serviceName, LocalDateTime.now());
            String dtoJson = objectMapper.writeValueAsString(dto);
            String response = keymasterServiceRestClient.verifyServiceRequest(apiToken, cronJobServiceName, dtoJson);

            VerifiedServiceDto verifiedServiceDto = objectMapper.readValue(response, VerifiedServiceDto.class);
            return verifiedServiceDto.authorizedStatus().equals("Service is authorized");
        }
        catch (WebApplicationException e) {
            Response response = e.getResponse();
            String errorBody = response.readEntity(String.class);
            logService.publish(String.format("Failed to verify with Keymaster. Status: %d, Error: %s",
                    response.getStatus(), errorBody), LoggingLevel.ERROR);
            logService.publish(String.format("Exception type: %s, Message: %s",
                    e.getClass().getName(), e.getMessage()), LoggingLevel.ERROR);
            return false;
        } catch (Exception e) {
            logService.publish(String.format("Unexpected error during verification: %s - %s",
                    e.getClass().getName(), e.getMessage()), LoggingLevel.ERROR);
            if (e.getCause() != null) {
                logService.publish(String.format("Caused by: %s - %s",
                        e.getCause().getClass().getName(), e.getCause().getMessage()), LoggingLevel.ERROR);
            }
            return false;
        }
    }
}
