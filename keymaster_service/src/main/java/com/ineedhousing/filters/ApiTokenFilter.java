package com.ineedhousing.filters;

import com.ineedhousing.models.enums.LoggingLevel;
import com.ineedhousing.services.ApiTokenValidationService;
import com.ineedhousing.services.LogService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;


@Provider
@Priority(1000)
public class ApiTokenFilter implements ContainerRequestFilter {

    @Inject
    ApiTokenValidationService apiTokenValidationService;

    @Inject
    LogService log;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        if (isOpenEndpoints(requestContext)) {
            return;
        }

        String apiToken = requestContext.getHeaderString("X-Api-Token");
        String serviceName = requestContext.getHeaderString("X-Service-Name");

        if (areValuesMissing(apiToken, serviceName)) {
            log.publish("Missing required headers \"X-Api-Token\" or \"X-Service-Name\". Unauthenticated request attempted:\n" + requestContext.getUriInfo().getPath(), LoggingLevel.WARN);
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Missing required headers \"X-Api-Token\" or \"X-Service-Name\"")
                            .build());
            return;
        }

        if (!apiTokenValidationService.isServiceAuthorized(apiToken, serviceName)) {
            log.publish("Invalid API token \"X-Api-Token\" or invalid service name \"X-Service-Name\". Unauthenticated request attempted:\n" + requestContext.getUriInfo().getPath(), LoggingLevel.WARN);
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity(String.format("%s is not a valid API token or %s is not registered", apiToken, serviceName))
                            .build());
        }
    }

    private boolean areValuesMissing(String apiToken, String serviceName) {
        return apiToken == null || apiToken.isEmpty() || serviceName == null || serviceName.isEmpty();
    }

    private boolean isOpenEndpoints(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getPath().equals("/v1/auth/register") || requestContext.getUriInfo().getPath().equals("/ping");
    }
}
