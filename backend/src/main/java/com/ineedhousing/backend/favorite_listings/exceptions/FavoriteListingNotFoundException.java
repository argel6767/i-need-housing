package com.ineedhousing.backend.favorite_listings.exceptions;

import com.ineedhousing.backend.exception.exceptions.NotFoundException;

public class FavoriteListingNotFoundException extends NotFoundException {

    public FavoriteListingNotFoundException(String message) {
        super(message);
    }
}
