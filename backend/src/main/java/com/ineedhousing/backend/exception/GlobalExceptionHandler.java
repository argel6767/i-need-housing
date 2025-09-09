package com.ineedhousing.backend.exception;

import com.ineedhousing.backend.exception.exceptions.BadRequestException;
import com.ineedhousing.backend.exception.exceptions.ServiceUnavailableException;
import com.ineedhousing.backend.model.FailedServiceInteractionDto;
import com.ineedhousing.backend.model.ServiceInteractionDto;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File size exceeds maximum allowed size");
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleRateLimitException(RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded. Please try again later.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exc) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exc.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<String> handleServletException(ServletException exc) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exc.getMessage());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<String> handleServiceUnavailableException(ServiceUnavailableException exc) {
        log.error("An external service could not complete a request. {}", exc.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(exc.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException me) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(me.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<FailedServiceInteractionDto> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error("A service call failed due to a client error. {}", ex.getMessage());
        String errorMessage = ex.getResponseBodyAsString();
        FailedServiceInteractionDto dto = new FailedServiceInteractionDto(errorMessage, LocalDateTime.now(), ex.getCause().toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<FailedServiceInteractionDto> handleHttpServerErrorException(HttpServerErrorException ex) {
        log.error("A service call failed due to a service error. {}", ex.getMessage());
        String errorMessage = ex.getResponseBodyAsString();
        FailedServiceInteractionDto dto = new FailedServiceInteractionDto(errorMessage, LocalDateTime.now(), ex.getCause().toString());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(dto);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<FailedServiceInteractionDto> handleResourceAccessException(ResourceAccessException ex) {
        log.error("A service call failed due to timeout. {}", ex.getMessage());
        FailedServiceInteractionDto dto = new FailedServiceInteractionDto(ex.getMessage(), LocalDateTime.now(), ex.getCause().toString());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(dto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        log.error("A bad request sent {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}