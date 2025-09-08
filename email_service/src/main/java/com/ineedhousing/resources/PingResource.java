package com.ineedhousing.resources;

import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.time.temporal.ChronoUnit;

@Path("/ping")
public class PingResource {

    @POST
    @RateLimit(windowUnit = ChronoUnit.MINUTES)
    public String ping() {
        return "pong";
    }
}
