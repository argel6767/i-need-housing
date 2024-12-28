package com.ineedhousing.backend.auth;


import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.requests.*;
import com.ineedhousing.backend.auth.responses.LoginResponse;
import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(authenticationService, jwtService);
    }

    @Test
    void testRegisterSuccessful() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("test@example.com", "password123");
        User expectedUser = new User("test@example.com", "encodedPassword123");
        when(authenticationService.signUp(request)).thenReturn(expectedUser);
        // Act
        ResponseEntity<User> response = authenticationController.register(request);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUser, response.getBody());
        verify(authenticationService).signUp(request);
    }

    @Test
    void testLoginSuccessful() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("test@example.com", "password123");
        User user = new User("test@example.com", "encodedPassword123");
        String token = "jwt.token.here";
        long expirationTime = 3600000L;
        when(authenticationService.authenticateUser(request)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);
        // Act
        ResponseEntity<LoginResponse> response = authenticationController.login(request);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        assertEquals(expirationTime, response.getBody().getExpiresIn());
        verify(authenticationService).authenticateUser(request);
        verify(jwtService).generateToken(user);
    }

    @Test
    void testVerifySuccessful() {
        // Arrange
        VerifyUserDto request = new VerifyUserDto("test@example.com", "123456");
        doNothing().when(authenticationService).verifyUser(request);
        // Act
        ResponseEntity<?> response = authenticationController.verify(request);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User verified!", response.getBody());
        verify(authenticationService).verifyUser(request);
    }

    @Test
    void testVerifyFailure() {
        // Arrange
        VerifyUserDto request = new VerifyUserDto("test@example.com", "123456");
        String errorMessage = "Invalid verification code";
        doThrow(new RuntimeException(errorMessage)).when(authenticationService).verifyUser(request);
        // Act
        ResponseEntity<?> response = authenticationController.verify(request);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(authenticationService).verifyUser(request);
    }

    @Test
    void testResendVerificationEmailSuccessful() {
        // Arrange
        String email = "test@example.com";
        ResendEmailDto emailDto = new ResendEmailDto(email);
        doNothing().when(authenticationService).resendVerificationEmail(email);
        // Act
        ResponseEntity<?> response = authenticationController.resend(emailDto);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verification code resent!", response.getBody());
        verify(authenticationService).resendVerificationEmail(email);
    }

    @Test
    void testResendVerificationEmailFailure() {
        // Arrange
        String email = "test@example.com";
        ResendEmailDto emailDto = new ResendEmailDto(email);
        String errorMessage = "Email already verified";
        doThrow(new RuntimeException(errorMessage)).when(authenticationService).resendVerificationEmail(email);
        // Act
        ResponseEntity<?> response = authenticationController.resend(emailDto);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(authenticationService).resendVerificationEmail(email);
    }

    @Test
    void testLoginFailureInvalidCredentials() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("test@example.com", "wrongpassword");
        when(authenticationService.authenticateUser(request))
                .thenThrow(new RuntimeException("Invalid credentials"));
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationController.login(request)
        );
        verify(authenticationService).authenticateUser(request);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void testRegisterFailureEmailTaken() {
        // Arrange
        AuthenticateUserDto request = new AuthenticateUserDto("taken@example.com", "password123");
        when(authenticationService.signUp(request))
                .thenThrow(new RuntimeException("Email already taken"));
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationController.register(request)
        );
        verify(authenticationService).signUp(request);
    }

    @Test
    void testSuccessfulPasswordChange() {
        // Arrange
        ChangePasswordDto request = new ChangePasswordDto();
        request.setEmail("testuser@example.com");
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass");
        User user = new User("testuser@example.com", "encodedPassword123");

        // Mock successful password change
        when(authenticationService.changePassword(request))
                .thenReturn(user);

        // Act
        ResponseEntity<?> response = authenticationController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testUserNotFound() {
        // Arrange
        ChangePasswordDto request = new ChangePasswordDto();
        request.setEmail("nonexistentuser@email.com");

        // Mock UsernameNotFoundException
        when(authenticationService.changePassword(request))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = authenticationController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUnauthorizedPasswordChange() {
        // Arrange
        ChangePasswordDto request = new ChangePasswordDto();
        request.setEmail("testuse@example.com");
        request.setOldPassword("wrongpassword");

        // Mock RuntimeException (e.g., invalid old password)
        when(authenticationService.changePassword(request))
                .thenThrow(new RuntimeException("Invalid password"));

        // Act
        ResponseEntity<?> response = authenticationController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid password", response.getBody());
    }

    @Test
    void testResetPasswordWithValidRequest() {
        //Arrange
        User user = new User("testuser@example.com", "password123");
        ForgotPasswordDto request = new ForgotPasswordDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setVerificationCode("123456");
        when(authenticationService.resetPassword(request)).thenReturn(user);

        //Act
        ResponseEntity<User> response = (ResponseEntity<User>) authenticationController.resetPassword(request);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testResetPasswordWithExpiredToken() {
        //Arrange
        ForgotPasswordDto request = new ForgotPasswordDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setVerificationCode("123456");
        when(authenticationService.resetPassword(request)).thenThrow(new ExpiredVerificationCodeException("Verification code expired, request another one"));

        //Act
        ResponseEntity<String> response = (ResponseEntity<String>) authenticationController.resetPassword(request);

        //Assert
        assertEquals(HttpStatus.GONE, response.getStatusCode());
        assertEquals("Verification code expired, request another one", response.getBody());
    }

    @Test
    void testResetPasswordWithInvalidToken() {
        //Arrange
        ForgotPasswordDto request = new ForgotPasswordDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setVerificationCode("123456");
        when(authenticationService.resetPassword(request)).thenThrow(new AuthenticationException("Invalid verification code"));

        //Act
        ResponseEntity<String> response = (ResponseEntity<String>) authenticationController.resetPassword(request);

        //Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid verification code", response.getBody());
    }

}