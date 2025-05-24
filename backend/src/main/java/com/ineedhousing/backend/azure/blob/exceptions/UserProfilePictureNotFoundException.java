package com.ineedhousing.backend.azure.blob.exceptions;

public class UserProfilePictureNotFoundException extends RuntimeException {
    public UserProfilePictureNotFoundException(String message) {
        super(message);
    }
}
