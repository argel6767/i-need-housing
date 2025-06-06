package com.ineedhousing.backend.auth;

import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.exceptions.InvalidPasswordException;
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

import lombok.extern.java.Log;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Holds the business logic for authenticating users and sending emails for verification codes
 */
@Log
@Service
public class AuthenticationService {
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

    /**
     * signs up user to app, will fail if the email is taken as they need to be unique
     */
    public User signUp(AuthenticateUserDto request) throws MessagingException {
        log.info("Signing up user");
        if (userRepository.findByEmail(request.getUsername()).isPresent()) {
            log.warning("User with email " + request.getUsername() + " already exists");
            throw new AuthenticationException("Email is already in use");
        }
        if (!isValidEmail(request.getUsername())) {
            log.warning("Invalid email " + request.getUsername());
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        if (!isValidPassword(request.getPassword())) {
            log.warning("Invalid password " + request.getPassword());
            throw new InvalidPasswordException(request.getPassword() + " is an invalid password");
        }
        log.info("Creating new user " + request.getUsername());
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        user.setAuthorities("ROLE_USER");
        String code = setVerificationCode(user);
        sendVerificationEmail(user, code);
        log.info("User " + request.getUsername() + " created");
        return userRepository.save(user);
    }



    /**
     * Checks whether an email is a valid one utilizing
     * org.apache.commons.validator.routines EmailValidator class
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    /**
     * This makes sure a password has the following characteristics:
     * (?=.*[a-z]): makes sure that there is at least one small letter
     * (?=.*[A-Z]): needs at least one capital letter
     * (?=.*\\d): requires at least one digit
     * (?=.*[@#$%^&+=!*?]): provides a guarantee of at least one special symbol
     * .{8,}: imposes the minimum length of 8 characters
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*?])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regExpn);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * generates a new verification code for user and sets it
     * @param user
     * @return
     */
    private String setVerificationCode(User user) {
        String verificationCode = generateVerificationCode();
        user.setCodeExpiry(LocalDateTime.now().plusMinutes(30));
        user.setVerificationCode(verificationCode);
        return verificationCode;
    }

    /**
     * authenticates user, usually when they are logging in
     * will throw an exception if the email is not tied to any user or the email has not been verified
     */
    public User authenticateUser(AuthenticateUserDto request) {
        log.info("Authenticating user " + request.getUsername());
        if (!isValidEmail(request.getUsername())) {
            log.warning("invalid email " + request.getUsername());
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        User user = getUser(request.getUsername());
        if (!user.isEnabled()) {
            log.warning("user " + request.getUsername() + " is not verified");
            throw new EmailVerificationException("Email not verified");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        user.setLastLogin(LocalDateTime.now());
        log.info("Authenticated user " + request.getUsername());
        return userRepository.save(user);
    }

    /**
     * verifies user by checking if request token is equal to the one in db
     * if so then the codeExpiry is set to null
     * and isEmailVerified to true
     */
    public void verifyUser(VerifyUserDto request) {
        User user = getUser(request.getEmail());
        if (user.getCodeExpiry() == null) {
            log.warning("user already verified: " + request.getEmail());
            throw new UserAlreadyVerifiedException("User is already verified");
        }
        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warning("verification code expired: " + request.getVerificationToken());
            throw new ExpiredVerificationCodeException("Verification code expired");
        }
        log.info("verifying user submitting token");
        if (request.getVerificationToken().equals(user.getVerificationCode())) {
            user.setCodeExpiry(null);
            user.setIsEnabled(true);
            log.info("user verified");
            userRepository.save(user);
        }
        else {
            log.warning("verification code invalid: " + request.getVerificationToken());
            throw new RuntimeException("Invalid verification token");
        }

    }

    /**
     * Grabs user from repository
     * @param email
     * @return
     */
    private User getUser(String email) {
        log.info("getting user " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * resends verification email to user but with new code
     * can be used if their last code expired
     */
    public void resendVerificationEmail(String email) throws MessagingException {
        User user = getUser(email);
        if (user.isEnabled()) {
            log.warning("user already verified: " + email);
            throw new EmailVerificationException("Email is already verified");
        }
        log.info("resending verification email with new code" + email);
        String code = setVerificationCode(user);
        sendVerificationEmail(user, code);
        userRepository.save(user);
    }

    /**
     * changes user's password to new one, only if:
     * they exist and if they send their correct current password
     */
    public User changePassword(ChangePasswordDto request) {
        User user = getUser(request.getEmail());
        String oldPasswordHash = user.getPasswordHash();
        if (!passwordEncoder.matches(request.getOldPassword(), oldPasswordHash)) {
            log.warning("old password does not match password hash");
            throw new RuntimeException("Invalid password");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        log.info("changing password");
        return userRepository.save(user);
    }

    /**
     * sends a user a verification code for changing one's, should they forget it.
     */

    public void sendForgottenPasswordVerificationCode(String email) throws MessagingException {
        log.info("sending forgotten password verification code for user " + email);
        User user = getUser(email);
        String code = setVerificationCode(user);
        sendResetPasswordEmail(user, code);
        userRepository.save(user);
    }

    /**
     * resets a user's password, only if:
     * code is not expired
     * and verification code is correct one in db
     */
    public User resetPassword(ForgotPasswordDto request) {
        User user = getUser(request.getEmail());
        if (user.getCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warning("verification code expired: " + request.getEmail());
            throw new ExpiredVerificationCodeException("Verification code expired, request another one");
        }
        if (!user.getVerificationCode().equals(request.getVerificationCode())) {
            log.warning("verification code invalid: " + request.getVerificationCode());
            throw new AuthenticationException("Invalid verification code");
        }
        user.setCodeExpiry(null);
        user.setVerificationCode(null);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        log.info("resetting password");
        return userRepository.save(user);
    }

    /**
     * formats and generates the verification email that will contain the verification code to the user
     */
    private void sendVerificationEmail(User user, String code) throws MessagingException {
       String email = user.getEmail();
       emailService.sendVerificationEmail(code, email);
       log.info("verification email sent for user " + user.getEmail());
    }

    /**
     * formats and generates the reset password email that will contain the verification code to the user
     */
    private void sendResetPasswordEmail(User user, String code) throws MessagingException {
       String email = user.getEmail();
       emailService.sendResetPasswordEmail(code,email);
       log.info("reset forgot password email sent for user " + user.getEmail());
    }


    /**
     * generates a random 6-digit number to be used as a verification code
     */
    private String generateVerificationCode() {
        log.info("generating verification code");
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000); //guaranteed 6-digit number
    }

}
