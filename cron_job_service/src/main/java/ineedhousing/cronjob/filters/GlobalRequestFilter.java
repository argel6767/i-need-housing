package ineedhousing.cronjob.filters;

import ineedhousing.cronjob.auth.ApiTokenValidationService;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;


@Provider
@Priority(1000)
public class GlobalRequestFilter implements ContainerRequestFilter {

    @Inject
    LogService logService;

    @Inject
    ApiTokenValidationService apiTokenValidationService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        if (requestContext.getUriInfo().getPath().equals("/ping")) {
            return;
        }

        String apiToken = requestContext.getHeaderString("X-Api-Token");
        String serviceName = requestContext.getHeaderString("X-Service-Name");

        if (ObjectUtils.allNull(apiToken, serviceName) || StringUtils.isAllBlank(apiToken, serviceName)) {
            logService.publish("Missing required headers \"X-Api-Token\" or \"X-Service-Name\". Unauthenticated request attempted:\n" + requestContext.getRequest().toString(), LoggingLevel.WARN);
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Missing required headers \"X-Api-Token\" or \"X-Service-Name\"")
                            .build());
            return;
        }

        if (!apiTokenValidationService.isServiceAuthenticated(apiToken, serviceName)) {
            logService.publish("Invalid API token  \"X-Api-Token\" or \"X-Service-Name\". Unauthenticated request attempted:\n" + requestContext.getRequest().toString(), LoggingLevel.WARN);
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity(String.format("%s is not a valid API token or %s is not registered", apiToken, serviceName))
                            .build());
        }
    }
}
