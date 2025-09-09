package com.ineedhousing.backend.exception.exceptions;

import com.ineedhousing.backend.model.FailedServiceInteractionDto;
import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {
    private final FailedServiceInteractionDto failedServiceInteractionDto;
    public ServiceUnavailableException(String message, FailedServiceInteractionDto failedServiceInteractionDto) {
        super(message);
        this.failedServiceInteractionDto = failedServiceInteractionDto;
    }
}
