package com.ineedhousing.backend.user;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@example.com", "hashedPassword");
        testUser.setId(1L);
        testUser.setUserType(UserType.INTERN);
    }

    @Test
    void getUserByEmail_whenUserExists_returnsUser() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.getUserByEmail(testUser.getEmail());

        // Assert
        assertNotNull(foundUser);
        assertEquals(testUser.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void getUserByEmail_whenUserDoesNotExist_throwsException() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail(testUser.getEmail()));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void updateUser_updatesAndReturnsUpdatedUser() {
        // Arrange
        User updatedUser = new User("test@example.com", "newHashedPassword");
        updatedUser.setUserType(UserType.NEW_GRAD);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.updateUser(updatedUser, "test@example.com");

        // Assert
        assertEquals(UserType.NEW_GRAD, result.getUserType());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void setUserType_updatesAndReturnsUpdatedUser() {
        // Arrange
        SetUserTypeRequest request = new SetUserTypeRequest(testUser.getEmail(), UserType.NEW_GRAD);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.setUserType(request);

        // Assert
        assertEquals(UserType.NEW_GRAD, result.getUserType());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }
}
