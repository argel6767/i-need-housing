package ineedhousing.cronjob.log;

import java.time.temporal.ChronoUnit;

import ineedhousing.cronjob.log.model.LogEventListWrapper;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("")
public class LogResource {

    @Inject
    LogService logService;

    @GET
    @Path("/logs")
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 10, window = 5, windowUnit = ChronoUnit.MINUTES)
    public LogEventListWrapper getLogs(@QueryParam("limit") Integer limit) {
        if (limit == null) {
            return logService.getMostRecentLogsCircularBuffer();
        }
        return logService.getMostRecentLogs(limit);
    }
}
