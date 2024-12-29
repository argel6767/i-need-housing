package com.ineedhousing.backend.favorite_listings.exceptions;

public class FavoriteListingNotFoundException extends RuntimeException{

    public FavoriteListingNotFoundException(String message) {
        super(message);
    }
}
