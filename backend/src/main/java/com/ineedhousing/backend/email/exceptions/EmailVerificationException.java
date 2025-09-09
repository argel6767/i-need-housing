package com.ineedhousing.backend.email.exceptions;

import java.io.Serial;

public class EmailVerificationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EmailVerificationException() {}
    public EmailVerificationException(String message) {}
}
