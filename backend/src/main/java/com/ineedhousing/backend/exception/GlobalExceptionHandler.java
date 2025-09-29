package com.ineedhousing.backend.exception;

import com.ineedhousing.backend.admin.exceptions.UnauthorizedAccessException;
import com.ineedhousing.backend.email.exceptions.EmailVerificationException;
import com.ineedhousing.backend.exception.exceptions.NotFoundException;
import com.ineedhousing.backend.exception.exceptions.BadRequestException;
import com.ineedhousing.backend.exception.exceptions.ServiceUnavailableException;
import com.ineedhousing.backend.model.FailedServiceInteractionDto;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.Optional;

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
        String cause = getExceptionCause(ex);
        FailedServiceInteractionDto dto = new FailedServiceInteractionDto(errorMessage, LocalDateTime.now(), cause);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<FailedServiceInteractionDto> handleHttpServerErrorException(HttpServerErrorException ex) {
        log.error("A service call failed due to a service error. {}", ex.getMessage());
        String errorMessage = ex.getResponseBodyAsString();
        String cause = getExceptionCause(ex);
        FailedServiceInteractionDto dto = new FailedServiceInteractionDto(errorMessage, LocalDateTime.now(), cause);
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(dto);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<FailedServiceInteractionDto> handleResourceAccessException(ResourceAccessException ex) {
        log.error("A service call failed due to timeout. {}", ex.getMessage());
        String cause = getExceptionCause(ex);
        FailedServiceInteractionDto dto = new FailedServiceInteractionDto(ex.getMessage(), LocalDateTime.now(), cause);
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

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<String> handleEmailVerificationException(EmailVerificationException ex) {
        log.error("An email verification failed due to {}", ex.getMessage());
        return  ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        log.error("An unauthorized access due to {}", ex.getMessage());
        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("An username not found due to {}", ex.getMessage());
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        log.error("An resource not found due to {}", ex.getMessage());
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("An invalid username or password due to {}", ex.getMessage());
        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.error("An invalid request due to lack of authorization {}", ex.getMessage());
        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    private String getExceptionCause(Exception ex) {
        Optional<Throwable> cause = Optional.ofNullable(ex.getCause());
        return cause.map(Throwable::getMessage).orElse("No cause present");
    }
}