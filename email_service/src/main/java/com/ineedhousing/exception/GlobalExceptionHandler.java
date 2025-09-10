package com.ineedhousing.exception;

import com.ineedhousing.models.responses.FailedRequestDto;
import io.quarkus.logging.Log;
import io.smallrye.faulttolerance.api.RateLimitException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.Instant;

@ApplicationScoped
public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Response handleBadRequest(BadRequestException exception) {
        return buildResponse(Response.Status.BAD_REQUEST, exception, "Bad Request");
    }

    @ServerExceptionMapper
    public Response handleIllegalArgument(IllegalArgumentException exception) {
        return buildResponse(Response.Status.BAD_REQUEST, exception, "Illegal Argument");
    }

    @ServerExceptionMapper
    public Response handleClientWebApp(ClientWebApplicationException exception) {
        return buildResponse(Response.Status.BAD_GATEWAY, exception, "Failed Downstream Request");
    }

    @ServerExceptionMapper
    public Response handleRateLimit(RateLimitException exception) {
        return buildResponse(Response.Status.TOO_MANY_REQUESTS, exception, "Rate Limit Exceeded");
    }

    @ServerExceptionMapper
    public Response handleGeneric(Throwable exception) {
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, exception, "Internal Server Error");
    }

    private Response buildResponse(
            Response.Status status,
            Throwable exception,
            String logPrefix
    ) {
        Log.errorf(exception, "%s: %s", logPrefix, exception.getMessage());

        FailedRequestDto dto = UtilFunctions.buildFailedRequestDto(exception);

        return Response.status(status)
                .entity(dto)
                .build();
    }
}

class UtilFunctions {
    public static FailedRequestDto buildFailedRequestDto(Throwable exception) {
        return new FailedRequestDto(
                exception.getMessage(),
                getTimeStamp(),
                exception.getClass().getName()
        );
    }

    private static String getTimeStamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}