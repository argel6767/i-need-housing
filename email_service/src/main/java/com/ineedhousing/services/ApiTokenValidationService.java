package com.ineedhousing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.configs.ServiceInteractionConfiguration;
import com.ineedhousing.models.ServiceVerificationDto;
import com.ineedhousing.models.VerifiedServiceDto;
import com.ineedhousing.rest_clients.KeymasterServiceRestClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;

@ApplicationScoped
public class ApiTokenValidationService {

    @Inject
    @RestClient
    KeymasterServiceRestClient keymasterServiceRestClient;

    @Inject
    ServiceInteractionConfiguration serviceInteractionConfiguration;

    @Inject
    ObjectMapper objectMapper;

    public boolean isServiceAuthenticated(String token, String serviceName) {
        try {
            Log.info("Verifying " + serviceName);
            String apiToken = serviceInteractionConfiguration.getApiToken();
            String cronJobServiceName = serviceInteractionConfiguration.getServiceName();


            Log.info(String.format("Verifying incoming service: %s", serviceName));
            ServiceVerificationDto dto = new ServiceVerificationDto(token, serviceName, LocalDateTime.now());
            String dtoJson = objectMapper.writeValueAsString(dto);
            String response = keymasterServiceRestClient.verifyServiceRequest(apiToken, cronJobServiceName, dtoJson);

            VerifiedServiceDto verifiedServiceDto = objectMapper.readValue(response, VerifiedServiceDto.class);
            return verifiedServiceDto.authorizedStatus().equals("Service is authorized");
        }
        catch (WebApplicationException e) {
            Response response = e.getResponse();
            String errorBody = response.readEntity(String.class);
            Log.error(String.format("Failed to verify with Keymaster. Status: %d, Error: %s",
                    response.getStatus(), errorBody));
            Log.error(String.format("Exception type: %s, Message: %s",
                    e.getClass().getName(), e.getMessage()));
            return false;
        } catch (Exception e) {
            Log.error(String.format("Unexpected error during verification: %s - %s",
                    e.getClass().getName(), e.getMessage()));
            if (e.getCause() != null) {
                Log.error(String.format("Caused by: %s - %s",
                        e.getCause().getClass().getName(), e.getCause().getMessage()));
            }
            return false;
        }
    }
}
