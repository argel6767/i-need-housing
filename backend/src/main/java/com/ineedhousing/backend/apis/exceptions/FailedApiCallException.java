package com.ineedhousing.backend.apis.exceptions;

public class FailedApiCallException extends RuntimeException {

    public FailedApiCallException(String message) {
        super(message);
    }
}
