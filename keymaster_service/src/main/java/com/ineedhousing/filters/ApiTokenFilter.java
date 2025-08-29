package com.ineedhousing.filters;

import com.ineedhousing.services.ApiTokenValidationService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import io.quarkus.logging.Log;

@Provider
@Priority(1000)
public class ApiTokenFilter implements ContainerRequestFilter {

    @Inject
    ApiTokenValidationService apiTokenValidationService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        if (isRegistrationEndpoint(requestContext)) {
            return;
        }

        String apiToken = requestContext.getHeaderString("X-Api-Token");
        String serviceName = requestContext.getHeaderString("X-Service-Name");

        if (areValuesMissing(apiToken, serviceName)) {
            Log.warn("Missing required headers \"X-Api-Token\" or \"X-Service-Name\". Unauthenticated request attempted:\n" + requestContext.getRequest().toString());
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Missing required headers \"X-Api-Token\" or \"X-Service-Name\"")
                            .build());
            return;
        }

        if (!apiTokenValidationService.isServiceAuthenticated(apiToken, serviceName)) {
            Log.warn("Invalid API token  \"X-Api-Token\" or \"X-Service-Name\". Unauthenticated request attempted:\n" + requestContext.getRequest().toString());
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity(String.format("%s is not a valid API token or %s is not registered", apiToken, serviceName))
                            .build());
        }
    }

    private boolean areValuesMissing(String apiToken, String serviceName) {
        return apiToken == null || apiToken.isEmpty() || serviceName == null || serviceName.isEmpty();
    }

    private boolean isRegistrationEndpoint(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getPath().equals("/v1/auth/register");
    }
}
