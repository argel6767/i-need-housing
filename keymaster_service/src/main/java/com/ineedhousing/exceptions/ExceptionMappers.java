package com.ineedhousing.exceptions;

import com.ineedhousing.models.FailedRequestDto;
import io.quarkus.logging.Log;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.Instant;

public class ExceptionMappers {

    @ServerExceptionMapper
    public Response mapBadRequestException(BadRequestException exception) {
        Log.error("Bad Request:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapIllegalStateException(IllegalStateException exception) {
        Log.error("Illegal Argument:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapSecurityException(SecurityException exception) {
        Log.error("Security Exception:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.FORBIDDEN).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapRateLimitException(RateLimitException exception) {
        Log.error("Rate Limit Exception:\n" + exception.getMessage());
        FailedRequestDto dto = new FailedRequestDto("Too many requests being sent. Try again later.",
                UtilFunctions.getTimeStamp(),
                exception.toString());
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapClientWebApplicationException(ClientWebApplicationException exception) {
        Log.error("Client Web Application Exception:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_GATEWAY).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapWebApplicationException(WebApplicationException exception) {
        Log.error("Web Application Exception:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(exception.getResponse().getStatus()).entity(dto).build();
    }

    @ServerExceptionMapper
    public Response mapGeneralException(Exception exception) {
        Log.error("Internal Server Error:\n" + exception);
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