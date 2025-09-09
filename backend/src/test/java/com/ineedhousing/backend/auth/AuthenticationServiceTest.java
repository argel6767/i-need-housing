package com.ineedhousing.backend.auth;

import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;
import com.ineedhousing.backend.auth.requests.ChangePasswordDto;
import com.ineedhousing.backend.auth.requests.ForgotPasswordDto;
import com.ineedhousing.backend.email.models.VerifyUserDto;
import com.ineedhousing.backend.email.v1.ClientEmailService;
import com.ineedhousing.backend.email.exceptions.EmailVerificationException;
import com.ineedhousing.backend.email.v2.EmailService;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ClientEmailService clientEmailService;
    @Mock
    private EmailService emailService;
    @Mock
    private TaskExecutor taskExecutor;

    private AuthenticationService authenticationService;

    private User successfulUser = new User();



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                userRepository,
                authenticationManager,
                passwordEncoder,
                clientEmailService,
                emailService,
                taskExecutor
        );

        successfulUser.setEmail("test@example.com");
        successfulUser.setPasswordHash("encodedPassword123");
        successfulUser.setVerificationCode("123456");
        successfulUser.setCodeExpiry(LocalDateTime.now().plusMinutes(10));

    }

    @Test
    void testSignUpSuccessful() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(successfulUser);
        // Act
        User result = authenticationService.signUp(request);
        // Assert
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertNotNull(result.getVerificationCode());
        assertNotNull(result.getCodeExpiry());
        verify(clientEmailService).sendEmail(any(), anyString(), anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAuthenticateUserSuccessful() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        User user = new User(email, password);
        user.setIsEnabled(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationService.authenticateUser(request)).thenReturn(user);
        // Act
        User result = authenticationService.authenticateUser(request);
        result.setEmail(email);
        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testAuthenticateUserEmailNotVerified() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        User user = new User(email, password);
        user.setIsEnabled(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailVerificationException.class, () ->
                authenticationService.authenticateUser(request)
        );
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void testVerifyUserSuccessful() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        User user = new User(email, "password");
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(10));

        VerifyUserDto request = new VerifyUserDto(email, code);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        authenticationService.verifyUser(request);

        // Assert
        assertTrue(user.isEnabled());
        assertNull(user.getCodeExpiry());
    }

    @Test
    void testVerifyUserExpiredCode() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        User user = new User(email, "password");
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().minusMinutes(10));

        VerifyUserDto request = new VerifyUserDto(email, code);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationService.verifyUser(request)
        );
        assertFalse(user.isEnabled());
    }

    @Test
    void testResendVerificationEmailSuccessful() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "password");
        user.setIsEnabled(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        // Act
        authenticationService.resendVerificationEmail(email);
        // Assert
        verify(clientEmailService).sendEmail(any(), any(), any());
        verify(userRepository).save(user);
        assertNotNull(user.getVerificationCode());
        assertNotNull(user.getCodeExpiry());
    }

    @Test
    void testResendVerificationEmailAlreadyVerified() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "password");
        user.setIsEnabled(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailVerificationException.class, () ->
                authenticationService.resendVerificationEmail(email)
        );
        verify(clientEmailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testVerifyUserInvalidCode() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        String wrongCode = "654321";
        User user = new User(email, "password");
        user.setVerificationCode(code);
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(10));

        VerifyUserDto request = new VerifyUserDto(email, wrongCode);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authenticationService.verifyUser(request)
        );
        assertFalse(user.isEnabled());
    }

    @Test
    void testGenerateVerificationCodeFormat() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        AuthenticateUserDto request = new AuthenticateUserDto(email, password);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User result = authenticationService.signUp(request);

        // Assert
        String code = result.getVerificationCode();
        assertNotNull(code);
        assertTrue(code.matches("\\d{6}")); // Verify it's a 6-digit number
    }

    @Test
    void testChangePasswordWithCorrectDetails() {
        String newPasswordHash = "hash";
        //Arrange
        ChangePasswordDto request = new ChangePasswordDto("test@example.com", "password123", "newPassword123");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(successfulUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(newPasswordHash);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        //Act
        User user = authenticationService.changePassword(request);

        //Assert
        assertNotNull(user);
        assertEquals(newPasswordHash, user.getPassword());
    }

    @Test
    void testChangePasswordWithInvalidEmail() {
        //Arrange
        ChangePasswordDto request = new ChangePasswordDto("test@example.com", "password123", "newPassword123");
        when(userRepository.findByEmail("test@example.com")).thenThrow(UsernameNotFoundException.class);

        //Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.changePassword(request);
        });
    }

    @Test
    void testChangePasswordWithInvalidPassword() {
        //Arrange
        ChangePasswordDto request = new ChangePasswordDto("test@example.com", "password123", "newPassword123");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(successfulUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        //Act and Assert
        assertThrows(RuntimeException.class, () -> {
            authenticationService.changePassword(request);
        });
    }

    @Test
    void testSendForgottenPasswordVerificationCodeWithValidEmail() throws MessagingException {
        //Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(successfulUser));

        //Act
        authenticationService.sendForgottenPasswordVerificationCode("test@example.com");

        //Assert
        verify(userRepository).save(any(User.class));
        verify(clientEmailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendForgottenPasswordVerificationCodeWithInvalidEmail() throws MessagingException {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(UsernameNotFoundException.class,
                () -> {
                    authenticationService.sendForgottenPasswordVerificationCode("test@example.com");});
    }

    @Test
    void testResetPasswordWithValidRequest() {
        //Arrange
        ForgotPasswordDto request = new ForgotPasswordDto("test@example.com", "newPassword", "123456");
        successfulUser.setCodeExpiry(LocalDateTime.now().plusMinutes(10));
        successfulUser.setVerificationCode("123456");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(successfulUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword"); // Mock password encoding

        //Act
        authenticationService.resetPassword(request);

        //Assert
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void testResetPasswordWithInvalidEmail() {
        //Arrange
        ForgotPasswordDto request = new ForgotPasswordDto("test@example.com", "newPassword", "123456");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.resetPassword(request);});
    }

    @Test
    void testResetPasswordWithExpiredCode() {
        //Arrange
        ForgotPasswordDto request = new ForgotPasswordDto("test@example.com", "newPassword", "123456");
        successfulUser.setCodeExpiry(LocalDateTime.now().minusMinutes(10));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(successfulUser));

        //Act and Assert
        assertThrows(ExpiredVerificationCodeException.class, () -> {
            authenticationService.resetPassword(request);});
    }

    @Test
    void testResetPasswordWithInvalidCode() {
        //Arrange
        ForgotPasswordDto request = new ForgotPasswordDto("test@example.com", "newPassword", "123456");
        successfulUser.setCodeExpiry(LocalDateTime.now().plusMinutes(10));
        successfulUser.setVerificationCode("000000"); //simulated different code
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(successfulUser));

        //Act and Assert
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.resetPassword(request);});
    }

}