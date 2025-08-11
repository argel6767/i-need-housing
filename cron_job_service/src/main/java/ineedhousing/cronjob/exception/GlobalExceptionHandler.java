package ineedhousing.cronjob.exception;

import java.time.Instant;

import org.jboss.resteasy.reactive.ClientWebApplicationException;

import static ineedhousing.cronjob.exception.UtilFunctions.buildFailedRequestDto;
import ineedhousing.cronjob.exception.model.FailedRequestDto;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;



@Provider
class ExceptionHandler implements ExceptionMapper<Exception> {
    @Inject
    LogService logService;

    @Override
    public Response toResponse(Exception exception) {
        logService.publish("Internal Server Error:\n" + exception, LoggingLevel.ERROR);
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.serverError().entity(dto).build();
    }
}

@Provider
class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {
    @Inject
    LogService logService;

    @Override
    public Response toResponse(BadRequestException exception) {
        logService.publish("Bad Request:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}

@Provider
class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {
    @Inject
    LogService logService;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        logService.publish("Illegal Argument:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = buildFailedRequestDto(exception);
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}

@Provider
class ClientWebApplicationExceptionHandler implements ExceptionMapper<ClientWebApplicationException> {
    @Inject
    LogService logService;

    @Override
    public Response toResponse(ClientWebApplicationException exception) {
        logService.publish("Failed Downstream Request Error:\n" + exception.getMessage(), LoggingLevel.ERROR);
        FailedRequestDto dto = buildFailedRequestDto(exception);
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
