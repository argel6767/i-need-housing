package com.ineedhousing.resources;

import com.ineedhousing.models.enums.LoggingLevel;
import com.ineedhousing.services.LogService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.time.temporal.ChronoUnit;

@Path("/ping")
public class PingResource {

    @Inject
    LogService log;

    @POST
    @RateLimit(windowUnit = ChronoUnit.MINUTES)
    public String ping() {
        log.publish("Ping endpoint hit", LoggingLevel.INFO);
        return "pong";
    }
}
