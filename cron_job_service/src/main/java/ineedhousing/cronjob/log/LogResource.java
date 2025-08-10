package ineedhousing.cronjob.log;

import ineedhousing.cronjob.log.model.LogEvent;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;

@Path("")
public class LogResource {

    @Inject
    LogService logService;

    @GET
    @Path("/logs")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<LogEvent> getLogs(@QueryParam("limit") Integer limit) {
        if (limit == null) {
            return logService.getMostRecentLogsCircularBuffer();
        }
        return logService.getMostRecentLogs(limit);
    }
}
