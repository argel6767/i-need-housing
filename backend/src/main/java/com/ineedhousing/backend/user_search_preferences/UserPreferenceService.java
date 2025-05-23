package com.ineedhousing.backend.user_search_preferences;

import java.io.InvalidObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ineedhousing.backend.jwt.JwtUtils;
import com.ineedhousing.backend.user_search_preferences.responses.FormattedUserPreferenceDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.geometry.GoogleGeoCodeApiService;
import com.ineedhousing.backend.geometry.dto.LocationAndCoordinatesDto;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user_search_preferences.exceptions.UserPreferenceNotFound;
import com.ineedhousing.backend.user_search_preferences.requests.NewFiltersDto;
import com.ineedhousing.backend.user_search_preferences.requests.RawCoordinateUserPreferenceRequest;
import com.ineedhousing.backend.user_search_preferences.requests.RawUserPreferencesDto;
import com.ineedhousing.backend.user_search_preferences.requests.UserPreferenceDto;
import com.ineedhousing.backend.user_search_preferences.utils.UserPreferenceBuilder;
import org.springframework.cache.annotation.Cacheable;


import lombok.extern.java.Log;

/**
 * Houses business logic for UserPreference
 */
@Service
@Log
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserService userService;
    private final GoogleGeoCodeApiService geoService;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserService userService, GoogleGeoCodeApiService geoService) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userService = userService;
        this.geoService = geoService;
    }

    /**
     * creates a UserPreference entity based on the raw request data
     * @param request
     * @return
     */
    public UserPreference createUserPreferences(UserPreferenceDto request, String email) {
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
        log.info(request.toString());
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
     * Creates a new UserPreference entry for User, is called when the dto contains addresses and not direct coordinates
     * google geo code is used to find the coordinates
     * @param request
     * @param email
     * @return
     * @throws InvalidObjectException
     */
    public UserPreference createUserPreference(RawUserPreferencesDto request, String email) throws InvalidObjectException {
        log.info("Handling following request:\n" + request.toString());
        if (areAnyRequiredFieldsNull(request)) {
            throw new InvalidObjectException("One or more required fields are null");
        }
        GeometryFactory factory = GeometrySingleton.getInstance();
        Point jobLocation = null;
        Point cityOfEmploymentLocation = null;
        if (request.getJobLocationAddress() != null) {
            double[] jobLocationCoordinates = geoService.getCoordinates(request.getJobLocationAddress());
            jobLocation = factory.createPoint(new Coordinate(jobLocationCoordinates[0], jobLocationCoordinates[1]));
        }
        LocationAndCoordinatesDto dto = geoService.getNameAndCoordinates(request.getCityOfEmploymentAddress());
        String cityOfEmploymentName = dto.cityName();
        double[] cityOfEmploymentCoordinates = dto.coordinates();
        cityOfEmploymentLocation = factory.createPoint(new Coordinate(cityOfEmploymentCoordinates[0], cityOfEmploymentCoordinates[1]));
        UserPreferenceBuilder builder = new UserPreferenceBuilder();
        UserPreference userPreferences = builder.addJobLocation(jobLocation)
            .addCityOfEmploymentCoordinates(cityOfEmploymentLocation)
            .addCityOfEmployment(cityOfEmploymentName)
            .addDesiredArea(Optional.ofNullable(jobLocation)
            .orElse(cityOfEmploymentLocation), request.getMaxRadius(), 32)
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

    private boolean areAnyRequiredFieldsNull(RawUserPreferencesDto request) {
        return request.getCityOfEmploymentAddress() == null || request.getMaxRadius() == null || request.getMaxRadius() == null ||
        request.getBedrooms() == null || request.getBathrooms() == null;
    }

    /**
     * get a User's UserPreference object
     * @param id
     * @return
     */
    @Cacheable(value = "preferences", key = "#id")
    public FormattedUserPreferenceDto getUserPreferences(Long id) {
        log.info("Fetching user preferences for " + id);
        User user = userService.getUserById(id);
        UserPreference userPreferences = user.getUserPreferences();
        log.info("fetching preferences: " + userPreferences);
        return getFormattedUserPreferenceDto(userPreferences);
    }


    /**
     * Formats the UserPreference entity in table into usable dto
     * by converting the Point objects to coordinates and Polygon object to coordinates
     * @param userPreferences
     * @return dto
     */
    private FormattedUserPreferenceDto getFormattedUserPreferenceDto(UserPreference userPreferences) {
        double[] jobLocationCoords = null;
        if (userPreferences.getJobLocation() != null) {
            Point jobLocation = userPreferences.getJobLocation();
            jobLocationCoords = new double[]{jobLocation.getY(), jobLocation.getX()}; //returns as lat, long, the preferred way
        }

        Point cityOfEmployment = userPreferences.getCityOfEmploymentCoordinates();
        double[] cityOfEmploymentCoords = new double[]{cityOfEmployment.getY(), cityOfEmployment.getX()};

        Polygon desiredAreaPolygon = userPreferences.getDesiredArea();
        List<Map<String, Double>> desiredArea = new ArrayList<>();
        for (Coordinate coord : desiredAreaPolygon.getCoordinates()) {
            desiredArea.add(Map.of("lat", coord.y, "lng", coord.x)); //Latitude is Y, Longitude is X
        }
        FormattedUserPreferenceDto dto = new FormattedUserPreferenceDto(userPreferences.getId(), jobLocationCoords, cityOfEmploymentCoords, userPreferences.getCityOfEmployment(),
                desiredArea, userPreferences.getMaxRadius(), userPreferences.getMaxRent() , userPreferences.getMinNumberOfBedrooms(), userPreferences.getMinNumberOfBathrooms(),
                userPreferences.getInternshipStart(), userPreferences.getInternshipEnd(), userPreferences.getUpdatedAt());
        return dto;
    }

    public UserPreference findUserPreferencesId(Long id) {
        return userPreferenceRepository.findById(id).orElseThrow( () -> new UserPreferenceNotFound("UserPreference with Id: "  + id + " does not exist."));
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

    /**
     * updates UserPreference object with updated values from the frontend filters
     * @param newPreferences
     * @return
     */
    public FormattedUserPreferenceDto updateUserPreferences(NewFiltersDto newPreferences) {
        UserPreference preferences = userPreferenceRepository.findById(newPreferences.getId())
        .orElseThrow( () -> new UserPreferenceNotFound("UserPreference with Id: "  + newPreferences.getId() + " does not exist."));

        log.info("updating user preferences with new values: "  + newPreferences + " for " + preferences.getId());

        //map the new preferences from filters in frontend
        preferences.setInternshipStart(newPreferences.getInternshipStart());
        preferences.setInternshipEnd(newPreferences.getInternshipEnd());
        preferences.setMaxRadius(newPreferences.getMaxRadius());
        preferences.setMaxRent(newPreferences.getMaxRent().doubleValue());
        preferences.setMinNumberOfBedrooms(newPreferences.getMinNumberOfBedrooms());
        preferences.setMinNumberOfBathrooms(newPreferences.getMinNumberOfBathrooms().doubleValue());
        preferences.setUpdatedAt(LocalDateTime.now());
        Long id = JwtUtils.getCurrentUserId();
        log.info("Invalidating cache for user: " + id);
        evictUserPreferencesCache(id);
        userPreferenceRepository.save(preferences);
        return getFormattedUserPreferenceDto(preferences);
    }

    //TODO FIGURE OUT WHY CACHING IS NOT RESETTING
    @CacheEvict(value = "preferences", key = "#id")
    public void evictUserPreferencesCache(Long id) {
        // This method just evicts the cache and doesn't need to do anything else
    }

}
