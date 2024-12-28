package com.ineedhousing.backend.configs;


import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationConfigurationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    private ApplicationConfiguration applicationConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationConfiguration = new ApplicationConfiguration(userRepository);
    }

    @Test
    void testUserDetailsServiceFindsExistingUser() {
        // Arrange
        String email = "test@example.com";
        User mockUserDetails = mock(User.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUserDetails));

        // Act
        UserDetailsService userDetailsService = applicationConfiguration.userDetailsService();
        UserDetails foundUser = userDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(foundUser);
        assertEquals(mockUserDetails, foundUser);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testUserDetailsServiceThrowsExceptionForNonExistentUser() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UserDetailsService userDetailsService = applicationConfiguration.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }

    @Test
    void testPasswordEncoder() {
        // Act
        BCryptPasswordEncoder passwordEncoder = applicationConfiguration.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        // Arrange
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthenticationManager);

        // Act
        AuthenticationManager authenticationManager = applicationConfiguration.authenticationManager(authenticationConfiguration);

        // Assert
        assertNotNull(authenticationManager);
        assertEquals(mockAuthenticationManager, authenticationManager);
    }

    @Test
    void testAuthenticationProvider() {
        // Act
        AuthenticationProvider authenticationProvider = applicationConfiguration.authenticationProvider();

        // Assert
        assertInstanceOf(DaoAuthenticationProvider.class, authenticationProvider);
        DaoAuthenticationProvider daoAuthProvider = (DaoAuthenticationProvider) authenticationProvider;
        assertNotNull(daoAuthProvider);
    }
}