package com.ineedhousing.exceptions;

import com.ineedhousing.models.dtos.FailedRequestDto;
import com.ineedhousing.models.enums.LoggingLevel;
import com.ineedhousing.services.LogService;
import io.quarkus.logging.Log;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.Instant;

public class ExceptionMappers {

    @Inject
    LogService log;

    @ServerExceptionMapper
    public Response mapBadRequestException(BadRequestException exception) {
        log.publish("Bad Request:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapIllegalStateException(IllegalStateException exception) {
        log.publish("Illegal Argument:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapSecurityException(SecurityException exception) {
        log.publish("Security Exception:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.FORBIDDEN).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapRateLimitException(RateLimitException exception) {
        log.publish("Rate Limit Exception:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = new FailedRequestDto("Too many requests being sent. Try again later.",
                UtilFunctions.getTimeStamp(),
                exception.toString());
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapClientWebApplicationException(ClientWebApplicationException exception) {
        log.publish("Client Web Application Exception:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_GATEWAY).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapWebApplicationException(WebApplicationException exception) {
        log.publish("Web Application Exception:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(exception.getResponse().getStatus()).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapGeneralException(Exception exception) {
        log.publish("Internal Server Error:\n" + exception.getMessage(), LoggingLevel.ERROR);
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