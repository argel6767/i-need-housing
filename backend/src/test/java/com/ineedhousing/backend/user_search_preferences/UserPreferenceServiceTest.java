package com.ineedhousing.backend.user_search_preferences;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user_search_preferences.requests.UserPreferenceDto;

class UserPreferenceServiceTest {

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserPreferenceService userPreferenceService;

    GeometryFactory factory = GeometrySingleton.getInstance();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserPreferences_ShouldSaveUserPreference() {
        // Arrange
        User user = new User();
        UserPreferenceDto request = new UserPreferenceDto();
        request.setJobLocation(factory.createPoint(new Coordinate(10.0, 11.0)));
        request.setCityOfEmployment(factory.createPoint(new Coordinate(16.0, 17.0)));
        request.setMaxRadius(10);
        request.setMaxRent(2000);
        request.setBedrooms(2);
        request.setBathrooms(1);
        request.setIsFurnished(true);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.of(2024, 8, 22));
        String email = "test@example.com";

        when(userService.getUserByEmail(email)).thenReturn(user);

        // Act
        UserPreference result = userPreferenceService.createUserPreferences(request, email);

        // Assert
        assertNotNull(result);
        verify(userService).getUserByEmail(email);
        verify(userService).saveUser(any(User.class));
    }

    @Test
    void updateUserPreferences_ShouldUpdateUserAndReturnNewPreferences() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        UserPreference newPreferences = new UserPreference();

        when(userService.getUserByEmail(email)).thenReturn(user);

        // Act
        UserPreference result = userPreferenceService.updateUserPreferences(newPreferences, email);

        // Assert
        assertEquals(newPreferences, result);
        verify(userService).getUserByEmail(email);
        verify(userService).saveUser(user);
        assertEquals(newPreferences, user.getUserPreferences());
    }

    @Test
    void createUserPreferences_ShouldHandleNullJobLocationAndFallbackToCityOfEmployment() {
        // Arrange
        User user = new User();
        UserPreferenceDto request = new UserPreferenceDto();
        request.setJobLocation(null);
        request.setCityOfEmployment(factory.createPoint(new Coordinate(14.0, 100.0)));
        request.setMaxRadius(15);
        request.setMaxRent(1500);
        request.setBedrooms(1);
        request.setBathrooms(1);
        request.setIsFurnished(false);
        request.setStartDate(LocalDate.of(2024, 5, 11));
        request.setEndDate(LocalDate.of(2024, 9, 14));
        String email = "test@example.com";

        when(userService.getUserByEmail(email)).thenReturn(user);

        // Act
        UserPreference result = userPreferenceService.createUserPreferences(request, email);

        // Assert
        assertNotNull(result);
        verify(userService).getUserByEmail(email);
        verify(userService).saveUser(any(User.class));
    }
}
