package com.ineedhousing.backend.user_search_preferences;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user_search_preferences.requests.RawUserPreferenceRequest;
import com.ineedhousing.backend.user_search_preferences.utils.UserPreferenceBuilder;

/**
 * Houses business logic for UserPreference
 */
@Service
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserService userService;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserService userService) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userService = userService;
    }

    /**
     * creates a UserPreference entity based of the raw request data
     * @param request
     * @return
     */
    public UserPreference createUserPreferences(RawUserPreferenceRequest request) {
        UserPreferenceBuilder builder = new UserPreferenceBuilder();
        UserPreference userPreferences = builder.addCityOfEmployment(request.getJobLocation())
        .addCityOfEmployment(request.getCityOfEmployment())
        .addDesiredArea(Optional.ofNullable(request.getJobLocation())
        .orElse(request.getCityOfEmployment()), request.getMaxRadius(), 32)
        .addMaxRadius(request.getMaxRadius())
        .addMaxRent(request.getMaxRent())
        .addMinNumberOfBedrooms(request.getBedrooms())
        .addMinNumberOfBathrooms(request.getBathrooms())
        .addIsFurnished(request.getIsFurnished())
        .addInternshipStart(request.getStartDate())
        .addInternshipEnd(request.getEndDate())
        .build();
        return userPreferenceRepository.save(userPreferences);
    }

    /**
     * updates UserPreference object in User entity with newPreferences
     * @param newPreferences
     * @param email
     * @return
     */
    public UserPreference updateUserPreferences(UserPreference newPreferences, String email) {
        User user = userService.getUserByEmail(email);
        user.setUserPreferences(newPreferences);
        userService.saveUser(user);
        return newPreferences;
    }

}
