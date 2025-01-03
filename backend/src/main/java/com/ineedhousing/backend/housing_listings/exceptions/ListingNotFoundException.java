package com.ineedhousing.backend.housing_listings.exceptions;

public class ListingNotFoundException extends RuntimeException{

    public ListingNotFoundException(String message) {
        super(message);
    }
}
