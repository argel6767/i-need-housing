package ineedhousing.cronjob;

import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
@Path("")
public class PingResource {


    @Inject
    LogService logService;

    /**
     * Pings service to wake up/check on it
     * @return
     */
    @POST
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(windowUnit = ChronoUnit.MINUTES)
    public String pingService() {
        logService.publish("Ping endpoint hit", LoggingLevel.INFO);
        return "Pong";
    }

}

