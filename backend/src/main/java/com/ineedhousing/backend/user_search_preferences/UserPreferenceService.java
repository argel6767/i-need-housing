package com.ineedhousing.backend.user_search_preferences;

import org.springframework.stereotype.Service;

import com.ineedhousing.backend.user.UserService;

@Service
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserService userService;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserService userService) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userService = userService;
    }

    public UserPreference createUserPreferences()
}
