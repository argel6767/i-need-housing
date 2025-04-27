package com.ineedhousing.backend.user_search_preferences;

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

import com.ineedhousing.backend.user_search_preferences.exceptions.UserPreferenceNotFound;
import com.ineedhousing.backend.user_search_preferences.requests.NewFiltersDto;
import com.ineedhousing.backend.user_search_preferences.requests.RawCoordinateUserPreferenceRequest;
import com.ineedhousing.backend.user_search_preferences.requests.RawUserPreferenceRequest;

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
     * @param email
     * @return
     */
    @PostMapping("/{email}")
    public ResponseEntity<?> createUserPreferences(@RequestBody RawUserPreferenceRequest request, @PathVariable String email) {
        try {
            UserPreference userPreference = userPreferenceService.createUserPreferences(request, email);
            return new ResponseEntity<>(userPreference,  HttpStatus.CREATED);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/coordinates/{email}")
    public ResponseEntity<?> createUserPreferencesWithCoordinates(@RequestBody RawCoordinateUserPreferenceRequest request, @PathVariable String email) {
        if (request.getCityOfEmployment() == null || request.getBathrooms() == null ||  request.getBedrooms() == null) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        try {
            UserPreference userPreference = userPreferenceService.createUserPreference(request, email);
            return new ResponseEntity<>(userPreference,  HttpStatus.CREATED);
        } catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
}

    /**
     * update UserPreference
     * @param userPreference
     * @param email
     * @return
     */
    @PutMapping("/{email}")
    public ResponseEntity<?> updateUserPreferences(@RequestBody UserPreference userPreference, @PathVariable String email) {
        try {
            UserPreference updatedPreferences = userPreferenceService.updateUserPreferences(userPreference, email);
            return ResponseEntity.ok(updatedPreferences);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * update UserPreference with filters
     * @param email
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
     * @param email
     * @return
     */
    @GetMapping("/{email}")
    public ResponseEntity<?> getPreferences(@PathVariable String email) {
        try {
            UserPreference userPreference = userPreferenceService.getUserPreferences(email);
            return ResponseEntity.ok(userPreference);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
