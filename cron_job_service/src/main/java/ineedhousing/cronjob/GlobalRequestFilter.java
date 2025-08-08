package ineedhousing.cronjob;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.io.IOException;
import java.util.Map;

@Provider
@Priority(1000)
public class GlobalRequestFilter implements ContainerRequestFilter {

    @ConfigProperty(name = "access.token.header")
    String accessToken;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String accessTokenRequest = requestContext.getHeaderString("Access-Header");

        if (accessTokenRequest == null || !accessTokenRequest.equals(accessToken)) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("Missing or invalid Access-Header")
                            .build()
            );
        }
    }
}