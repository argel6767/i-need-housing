package com.ineedhousing.backend.housing_listings.exceptions;

public class NoListingsFoundInAreaException extends RuntimeException{
    
    public NoListingsFoundInAreaException(String message) {
        super(message);
    }
}
