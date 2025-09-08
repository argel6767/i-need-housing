package com.ineedhousing.exception;


import io.quarkus.logging.Log;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import main.java.com.ineedhousing.exception.model.FailedRequestDto;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.time.Instant;

import static com.ineedhousing.exception.UtilFunctions.buildFailedRequestDto;

@Provider
class ExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        Log.error("Internal Server Error:\n" + exception);
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.serverError().entity(dto).build();
    }
}

@Provider
class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {
   @Override
    public Response toResponse(BadRequestException exception) {
        Log.error("Bad Request:\n" + exception.getMessage());
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}

@Provider
class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        Log.error("Illegal Argument:\n" + exception.getMessage());
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}

@Provider
class ClientWebApplicationExceptionHandler implements ExceptionMapper<ClientWebApplicationException> {
    @Override
    public Response toResponse(ClientWebApplicationException exception) {
        Log.error("Failed Downstream Request Error:\n" + exception.getMessage());
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_GATEWAY).entity(dto).build();
    }
}

@Provider
class RateLimitExceptionHandler implements ExceptionMapper<RateLimitException> {
    @Override
    public Response toResponse(RateLimitException exception) {
        Log.error("Rate Limit Exception:\n" + exception.getMessage());
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(dto).build();
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
