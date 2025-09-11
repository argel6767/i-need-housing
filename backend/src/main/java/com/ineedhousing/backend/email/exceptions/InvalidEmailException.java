package com.ineedhousing.backend.email.exceptions;

import com.ineedhousing.backend.exception.exceptions.BadRequestException;

public class InvalidEmailException extends BadRequestException {

    public InvalidEmailException(String message) {
        super(message);
    }
}
