package com.ineedhousing.exceptions;

import com.ineedhousing.models.FailedRequestDto;
import io.quarkus.logging.Log;

import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.time.Instant;

import static com.ineedhousing.exceptions.UtilFunctions.getTimeStamp;

@Provider
class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Log.error("Internal Server Error:\n" + exception);
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.serverError().entity(dto).build();
    }

}

@Provider
class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {
        Log.error("Bad Request:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}

@Provider
class IllegalStateExceptionHandler implements ExceptionMapper<IllegalStateException> {

    @Override
    public Response toResponse(IllegalStateException exception) {
        Log.error("Illegal Argument:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}

@Provider
class SecurityExceptionHandler implements ExceptionMapper<SecurityException> {
    @Override
    public Response toResponse(SecurityException exception) {
        Log.error("Security Exception:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.FORBIDDEN).entity(dto).build();
    }
}

@Provider
class RateLimitExceptionHandler implements ExceptionMapper<RateLimitException> {
    @Override
    public Response toResponse(RateLimitException exception) {
        Log.error("Rate Limit Exception:\n" + exception.getMessage());
        FailedRequestDto dto = new FailedRequestDto("Too many requests being sent. Try again later.", getTimeStamp(), exception.toString());
        return Response.status(Response.Status.TOO_MANY_REQUESTS).entity(dto).build();
    }
}

@Provider
class ClientWebApplicationExceptionHandler implements ExceptionMapper<ClientWebApplicationException> {
    @Override
    public Response toResponse(ClientWebApplicationException exception) {
        Log.error("Client Web Application Exception:\n" + exception.getMessage());
        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_GATEWAY).entity(dto).build();
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
