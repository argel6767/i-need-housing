package com.ineedhousing.backend.apis.exceptions;

public class NoListingsFoundException extends RuntimeException {

    public NoListingsFoundException(String message) {
        super(message);
    }
}
