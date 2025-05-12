package com.ineedhousing.backend.user_search_preferences;

import com.ineedhousing.backend.user_search_preferences.requests.UserPreferenceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPreferenceControllerTest {

    @Mock
    private UserPreferenceService userPreferenceService;

    @InjectMocks
    private UserPreferenceController userPreferenceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserPreferences_Success() {
        UserPreferenceDto request = new UserPreferenceDto();
        String email = "test@example.com";
        UserPreference mockPreference = new UserPreference();

        when(userPreferenceService.createUserPreferences(request, email)).thenReturn(mockPreference);

        ResponseEntity<?> response = userPreferenceController.createUserPreferences(request);

        assertEquals(ResponseEntity.ok(mockPreference), response);
        verify(userPreferenceService, times(1)).createUserPreferences(request, email);
    }

    @Test
    void createUserPreferences_UserNotFound() {
        UserPreferenceDto request = new UserPreferenceDto();
        String email = "nonexistent@example.com";

        when(userPreferenceService.createUserPreferences(request, email)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userPreferenceController.createUserPreferences(request);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody());
        verify(userPreferenceService, times(1)).createUserPreferences(request, email);
    }

    @Test
    void updateUserPreferences_Success() {
        UserPreference userPreference = new UserPreference();
        String email = "test@example.com";
        UserPreference updatedPreference = new UserPreference();

        when(userPreferenceService.updateUserPreferences(userPreference, email)).thenReturn(updatedPreference);

        ResponseEntity<?> response = userPreferenceController.updateUserPreferences(userPreference);

        assertEquals(ResponseEntity.ok(updatedPreference), response);
        verify(userPreferenceService, times(1)).updateUserPreferences(userPreference, email);
    }

    @Test
    void updateUserPreferences_UserNotFound() {
        UserPreference userPreference = new UserPreference();
        String email = "nonexistent@example.com";

        when(userPreferenceService.updateUserPreferences(userPreference, email)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userPreferenceController.updateUserPreferences(userPreference);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody());
        verify(userPreferenceService, times(1)).updateUserPreferences(userPreference, email);
    }

    @Test
    void getPreferences_Success() {
        String email = "test@example.com";
        UserPreference mockPreference = new UserPreference();

        when(userPreferenceService.getUserPreferences(email)).thenReturn(null);

        ResponseEntity<?> response = userPreferenceController.getPreferences();

        assertEquals(ResponseEntity.ok(mockPreference), response);
        verify(userPreferenceService, times(1)).getUserPreferences(email);
    }

    @Test
    void getPreferences_UserNotFound() {
        String email = "nonexistent@example.com";

        when(userPreferenceService.getUserPreferences(email)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userPreferenceController.getPreferences();

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody());
        verify(userPreferenceService, times(1)).getUserPreferences(email);
    }
}
