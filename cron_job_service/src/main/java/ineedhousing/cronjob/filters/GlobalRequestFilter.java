package ineedhousing.cronjob.filters;

import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;

@Provider
@Priority(1000)
public class GlobalRequestFilter implements ContainerRequestFilter {

    @Inject
    LogService logService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String accessToken = System.getenv("ACCESS_TOKEN_HEADER");

        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new RuntimeException("ACCESS_TOKEN_HEADER environment variable not found or empty!");
        }

        String accessTokenRequest = requestContext.getHeaderString("Access-Header");

        if (accessTokenRequest == null || !accessTokenRequest.equals(accessToken)) {
            logService.publish("Unauthenticated request attempt:\n" + requestContext.getRequest().toString(), LoggingLevel.WARN);
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("Missing or invalid Access-Header")
                            .build()
            );
        }
    }
}