package com.ineedhousing.backend.housing_listings.exceptions;

public class NoListingFoundException extends RuntimeException{

    public NoListingFoundException(String message) {
        super(message);
    }
}
