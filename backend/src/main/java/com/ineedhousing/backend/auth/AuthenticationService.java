package com.ineedhousing.backend.auth;


import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.exceptions.UserAlreadyVerifiedException;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;
import com.ineedhousing.backend.auth.requests.ChangePasswordDto;
import com.ineedhousing.backend.auth.requests.ForgotPasswordDto;
import com.ineedhousing.backend.auth.requests.VerifyUserDto;
import com.ineedhousing.backend.email.EmailService;
import com.ineedhousing.backend.email.EmailVerificationException;
import com.ineedhousing.backend.email.InvalidEmailException;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;
import jakarta.mail.MessagingException;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Consumer;

/**
 * holds the business logic for authenticating users and sending emails for verification codes
 */
@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /*
     * signs up user to app, will fail if the email is taken as they need to be unique
     */
    public User signUp(AuthenticateUserDto request) {
        if (userRepository.findByEmail(request.getUsername()).isPresent()) {
            throw new AuthenticationException("Email is already in use");
        }
        if (!isValidEmail(request.getUsername())) {
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        log.info("Creating new user {}", request.getUsername());
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        user.setAuthorities("ROLE_USER");
        setVerificationCodeAndSendIt(user, this::sendVerificationEmail);
        return userRepository.save(user);
    }

    /*
     * holds the verification code setting and sending for reduced repeat code
     */
    private void setVerificationCodeAndSendIt(User user, Consumer<User> sendEmail) {
        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(30));
        sendEmail.accept(user);
    }

    private boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    /*
     * authenticates user, usually when they are logging in
     * will throw an exception if the email is not tied to any user or the email has not been verified
     */
    public User authenticateUser(AuthenticateUserDto request) {
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (!user.isEnabled()) {
            throw new EmailVerificationException("Email not verified");
        }
        if (!isValidEmail(request.getUsername())) {
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    /*
     * verifies user by checking if request token is equal to the one in db
     * if so then the codeExpiry is set to null
     * and isEmailVerified to true
     */
    public void verifyUser(VerifyUserDto request) {
        User user = getUser(request.getEmail());
        if (user.getCodeExpiry() == null) {
            throw new UserAlreadyVerifiedException("User is already verified");
        }
        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new ExpiredVerificationCodeException("Verification code expired");
        }
        if (request.getVerificationToken().equals(user.getVerificationCode())) {
            user.setCodeExpiry(null);
            user.setIsEnabled(true);
            userRepository.save(user);
        }
        else {
            throw new RuntimeException("Invalid verification token");
        }

    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /*
     * resends verification email to user but with new code
     * can be used if their last code expired
     */
    public void resendVerificationEmail(String email) {
        User user = getUser(email);
        if (user.isEnabled()) {
            throw new EmailVerificationException("Email is already verified");
        }
        setVerificationCodeAndSendIt(user, this::sendVerificationEmail);
        userRepository.save(user);
    }

    /*
     * changes user's password to new one, only if:
     * they exist and if they send their correct current password
     */
    public User changePassword(ChangePasswordDto request) {
        User user = getUser(request.getEmail());
        String oldPasswordHash = user.getPasswordHash();
        if (!passwordEncoder.matches(request.getOldPassword(), oldPasswordHash)) {
            throw new RuntimeException("Invalid password");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    /*
     * sends a user a verification code for changing one's, should they forget it.
     */
    public void sendForgottenPasswordVerificationCode(String email) {
        User user = getUser(email);
        setVerificationCodeAndSendIt(user, this::sendResetPasswordEmail);
        userRepository.save(user);
    }

    /*
     * resets a user's password, only if:
     * code is not expired
     * and verification code is correct one in db
     */
    public User resetPassword(ForgotPasswordDto request) {
        User user = getUser(request.getEmail());
        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new ExpiredVerificationCodeException("Verification code expired, request another one");
        }
        if (!user.getVerificationCode().equals(request.getVerificationCode())) {
            throw new AuthenticationException("Invalid verification code");
        }
        user.setCodeExpiry(null);
        user.setVerificationCode(null);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    /*
     * formats and generates the verification email that will contain the verification code to the user
     */
    private void sendVerificationEmail(User user) {
        String to = user.getUsername();
        String subject = "Verification Email";
        String code = user.getVerificationCode();
        String body = String.format("""
            <!DOCTYPE html>
            <html>
            <body>
                <h1>Welcome to INeedHousing!</h1>
                <p>Enter the verification code below:</p>
                <p>%s</p>
                <p>If you didn't request this code, please ignore this email or contact our support team.</p>
            </body>
            </html>""", code);
        sendEmail(to, subject, body);
    }

    private void sendResetPasswordEmail(User user) {
        String to = user.getUsername();
        String subject = "Reset Password";
        String code = user.getVerificationCode();
        String body = String.format("""
            <!DOCTYPE html>
            <html>
            <body>
                <h1>Forgot your password?</h1>
                <p>Reset your password with the verification code below:</p>
                <p class="verification-code">%s</p>
                <p>If you didn't request this code, please ignore this email or contact our support team.</p>
            </body>
            </html>""", code);
        sendEmail(to, subject, body);
    }

    /*
     * holds the try catch logic of sending the email
     */
    private void sendEmail(String to, String subject, String body) {
        try{
            emailService.sendEmail(to, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending verification email", e);
        }
    }

    /*
     * generates a random 6-digit number to be used as a verification code
     */
    private String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000); //guaranteed 6-digit number
    }

}
