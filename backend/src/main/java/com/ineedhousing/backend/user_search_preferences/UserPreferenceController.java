package com.ineedhousing.backend.user_search_preferences;

import java.io.InvalidObjectException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.geometry.exceptions.ErroredGeoCodeAPICallException;
import com.ineedhousing.backend.jwt.JwtUtils;
import com.ineedhousing.backend.user_search_preferences.exceptions.UserPreferenceNotFound;
import com.ineedhousing.backend.user_search_preferences.requests.NewFiltersDto;
import com.ineedhousing.backend.user_search_preferences.requests.RawCoordinateUserPreferenceRequest;
import com.ineedhousing.backend.user_search_preferences.requests.RawUserPreferencesDto;
import com.ineedhousing.backend.user_search_preferences.requests.UserPreferenceDto;

import lombok.extern.java.Log;



/**
 * Houses UserPreference endpoints
 */
@RestController
@RequestMapping("/preferences")
@Log
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * create new UserPreference
     * @param request
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> createUserPreferences(@RequestBody UserPreferenceDto request) {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            UserPreference userPreference = userPreferenceService.createUserPreferences(request, email);
            return new ResponseEntity<>(userPreference,  HttpStatus.CREATED);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (FailedApiCallException face) {
            return new ResponseEntity<>(face.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (ErroredGeoCodeAPICallException egcae) {
            return new ResponseEntity<>(egcae.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/coordinates")
    public ResponseEntity<?> createUserPreferencesWithCoordinates(@RequestBody RawCoordinateUserPreferenceRequest request) {
        if (request.getCityOfEmployment() == null || request.getBathrooms() == null ||  request.getBedrooms() == null) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        try {
            String email = JwtUtils.getCurrentUserEmail();
            UserPreference userPreference = userPreferenceService.createUserPreference(request, email);
            return new ResponseEntity<>(userPreference,  HttpStatus.CREATED);
        } catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (FailedApiCallException face) {
            return new ResponseEntity<>(face.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (ErroredGeoCodeAPICallException egcae) {
            return new ResponseEntity<>(egcae.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
}

@PostMapping("/addresses")
    public ResponseEntity<?> createUserPreferenceWithAddresses(@RequestBody RawUserPreferencesDto request) {
        log.info("Beginning creation with request" + request.toString());
        try {
            String email = JwtUtils.getCurrentUserEmail();
            UserPreference userPreference = userPreferenceService.createUserPreference(request, email);
            return new ResponseEntity<>(userPreference, HttpStatus.CREATED);
        }
        catch (InvalidObjectException ioe) {
            return new ResponseEntity<>(ioe.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (FailedApiCallException face) {
            return new ResponseEntity<>(face.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (ErroredGeoCodeAPICallException egcae) {
            return new ResponseEntity<>(egcae.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * update UserPreference
     * @param userPreference
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<?> updateUserPreferences(@RequestBody UserPreference userPreference) {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            UserPreference updatedPreferences = userPreferenceService.updateUserPreferences(userPreference, email);
            return ResponseEntity.ok(updatedPreferences);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * update UserPreference with filters
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<?> updateUserPreferences(@RequestBody NewFiltersDto request) {
        try {
            UserPreference updatedPreferences = userPreferenceService.updateUserPreferences(request);
            return ResponseEntity.ok(updatedPreferences);
        }
        catch (UserPreferenceNotFound unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * get UserPreference
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<?> getPreferences() {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            UserPreference userPreference = userPreferenceService.getUserPreferences(email);
            return ResponseEntity.ok(userPreference);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
