package com.ineedhousing.backend.user_search_preferences;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.user_search_preferences.requests.RawUserPreferenceRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    @PostMapping("/{email}")
    public ResponseEntity<?> createUserPreferences(@RequestBody RawUserPreferenceRequest request, @PathVariable String email) {
        try {
            UserPreference userPreference = userPreferenceService.createUserPreferences(request, email);
            return ResponseEntity.ok(userPreference);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> updateUserPreferences(@RequestBody UserPreference userPreference, @PathVariable String email) {
        try {
            UserPreference updatedPreferences = userPreferenceService.updateUserPreferences(userPreference, email);
        }
        catch 
    }
    
    
}
