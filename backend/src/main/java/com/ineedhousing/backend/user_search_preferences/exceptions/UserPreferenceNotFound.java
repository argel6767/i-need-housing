package com.ineedhousing.backend.user_search_preferences.exceptions;

import com.ineedhousing.backend.exception.exceptions.NotFoundException;

public class UserPreferenceNotFound extends NotFoundException {

    public UserPreferenceNotFound(String message) {
        super(message);
    }

}
