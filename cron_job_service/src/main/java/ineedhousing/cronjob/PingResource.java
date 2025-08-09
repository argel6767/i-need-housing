package ineedhousing.cronjob;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import java.util.List;

@ApplicationScoped
@Path("")
public class PingResource {

    @Inject
    @Any
    Instance<HealthCheck> healthChecks;

    /**
     * Pings service to wake up/check on it
     * @return
     */
    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public HealthPing pingService() {
        List<HealthCheckResponse> healthCheckResponses = healthChecks.stream()
                .map(HealthCheck::call)
                .toList();
        return new HealthPing("Cron_Job_Service pinged. Health info:", healthCheckResponses);
    }

}

record HealthPing(String message, List<HealthCheckResponse> healthCheckResponses) {}
