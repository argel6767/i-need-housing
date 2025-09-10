package ineedhousing.cronjob.exception;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import ineedhousing.cronjob.exception.model.FailedRequestDto;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;

@ApplicationScoped
public class GlobalExceptionHandler {

    @Inject
    LogService logService;

    @ServerExceptionMapper
    public Response handleBadRequest(BadRequestException exception) {
        logService.publish("Bad Request:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response handleIllegalArgument(IllegalArgumentException exception) {
        logService.publish("Illegal Argument:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response handleClientWebApplication(ClientWebApplicationException exception) {
        logService.publish("Failed Downstream Request Error:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_GATEWAY).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response handleRateLimit(RateLimitException exception) {
        logService.publish("Rate Limit Exception:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response handleAny(Exception exception) {
        logService.publish("Internal Server Error:\n" + exception, LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.serverError().entity(dto).build();
    }
}

class UtilFunctions {
    public static FailedRequestDto buildFailedRequestDto(Exception exception) {
        return new FailedRequestDto(exception.getMessage(), getTimeStamp(), exception.toString());
    }

    public static String getTimeStamp() {
        Long time = Instant.now().toEpochMilli();
        return String.valueOf(time);
    }
}