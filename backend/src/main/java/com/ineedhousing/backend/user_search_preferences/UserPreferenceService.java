package com.ineedhousing.backend.user_search_preferences;

import java.util.Optional;
import java.util.stream.Stream;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user_search_preferences.requests.RawCoordinateUserPreferenceRequest;
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
     * creates a UserPreference entity based on the raw request data
     * @param request
     * @return
     */
    public UserPreference createUserPreferences(RawUserPreferenceRequest request, String email) {
        UserPreferenceBuilder builder = new UserPreferenceBuilder();
        UserPreference userPreferences = builder.addJobLocation(request.getJobLocation())
        .addCityOfEmploymentCoordinates(request.getCityOfEmployment())
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
        User user = userService.getUserByEmail(email);
        user.setUserPreferences(userPreferences);
        userService.saveUser(user);
        return userPreferences;
    }

    /**
     * creates a UserPreference entity based on the raw coordinate request data
     * @param request
     * @return
     */
    public UserPreference createUserPreference(RawCoordinateUserPreferenceRequest request, String email) {
        UserPreferenceBuilder builder = new UserPreferenceBuilder();
        GeometryFactory factory = GeometrySingleton.getInstance();
        Point jobLocation = null;
        if (request.getJobLocationCoordinates() != null) {
            Double[] coordinates = request.getJobLocationCoordinates();
             jobLocation = factory.createPoint(new Coordinate(coordinates[1], coordinates[0]));
        }
        Double[] coordinates = request.getCityOfEmploymentCoordinates();
        Point cityOfEmployment = factory.createPoint(new Coordinate(coordinates[1], coordinates[0]));
        UserPreference userPreferences = builder.addJobLocation(jobLocation)
        .addCityOfEmploymentCoordinates(cityOfEmployment)
        .addCityOfEmployment(request.getCityOfEmployment())
        .addDesiredArea(Optional.ofNullable(jobLocation)
        .orElse(cityOfEmployment), request.getMaxRadius(), 32)
        .addMaxRadius(request.getMaxRadius())
        .addMaxRent(request.getMaxRent())
        .addMinNumberOfBedrooms(request.getBedrooms())
        .addMinNumberOfBathrooms(request.getBathrooms())
        .addIsFurnished(request.getIsFurnished())
        .addInternshipStart(request.getStartDate())
        .addInternshipEnd(request.getEndDate())
        .build();
        User user = userService.getUserByEmail(email);
        user.setUserPreferences(userPreferences);
        userService.saveUser(user);
        return userPreferences;
    }

    /**
     * get a User's UserPreference object
     * @param email
     * @return
     */
    public UserPreference getUserPreferences(String email) {
        User user = userService.getUserByEmail(email);
        return user.getUserPreferences();
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
