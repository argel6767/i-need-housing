package com.ineedhousing.backend.user;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@example.com", "hashedPassword");
        testUser.setId(1L);
        testUser.setUserType(UserType.INTERN);
    }

    @Test
    void getUser_whenUserExists_returnsUser() {
        // Arrange
        String email = "test@example.com";
        when(userService.getUserByEmail(email)).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = userController.getUser(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void getUser_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        String email = "test@example.com";
        when(userService.getUserByEmail(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = userController.getUser(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void updateUser_whenUserExists_updatesAndReturnsUser() {
        // Arrange
        String email = "test@example.com";
        User updatedUser = new User("test@example.com", "newHashedPassword");
        when(userService.updateUser(testUser, email)).thenReturn(updatedUser);

        // Act
        ResponseEntity<?> response = userController.updateUser(testUser, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).updateUser(testUser, email);
    }

    @Test
    void updateUser_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        String email = "test@example.com";
        when(userService.updateUser(testUser, email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = userController.updateUser(testUser, email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).updateUser(testUser, email);
    }

    @Test
    void setUserType_whenUserExists_updatesAndReturnsUser() {
        // Arrange
        SetUserTypeRequest request = new SetUserTypeRequest("test@example.com", UserType.NEW_GRAD);
        when(userService.setUserType(request)).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = userController.setUserType(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).setUserType(request);
    }

    @Test
    void setUserType_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        SetUserTypeRequest request = new SetUserTypeRequest("test@example.com", UserType.NEW_GRAD);
        when(userService.setUserType(request)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = userController.setUserType(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).setUserType(request);
    }
}
