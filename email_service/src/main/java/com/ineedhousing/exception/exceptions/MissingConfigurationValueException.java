package com.ineedhousing.exception.exceptions;

public class MissingConfigurationValueException extends RuntimeException {
    public MissingConfigurationValueException(String message) {
        super(message);
    }
}
