package com.ineedhousing.backend.housing_listings.exceptions;

import com.ineedhousing.backend.exception.exceptions.NotFoundException;

public class NoListingFoundException extends NotFoundException {

    public NoListingFoundException(String message) {
        super(message);
    }
}
