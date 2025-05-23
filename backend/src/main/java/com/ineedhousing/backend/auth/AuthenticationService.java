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
        if (!isValidEmail(request.getUsername())) {
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (!user.isEnabled()) {
            throw new EmailVerificationException("Email not verified");
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
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Verify Your Account</title>
                  <style>
                    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
                    :root {
                      --primary: #176087;
                      --foreground: #000000;
                      --slate-50: #f8fafc;
                      --slate-100: #f1f5f9;
                      --slate-200: #e2e8f0;
                    }
                    body {
                      font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                      margin: 0;
                      padding: 0;
                      line-height: 1.6;
                      background-color: #f5f5f5;
                    }
                    .container {
                      max-width: 600px;
                      margin: 0 auto;
                      background-color: #ffffff;
                      border-radius: 8px;
                      overflow: hidden;
                      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                      padding: 24px;
                      background-color: var(--primary);
                      text-align: center;
                    }
                    .header img {
                      height: 40px;
                    }
                    .content {
                      padding: 32px 24px;
                      background-color: #ffffff;
                    }
                    .title {
                      font-size: 20px;
                      font-weight: 600;
                      color: var(--foreground);
                      margin-top: 0;
                      margin-bottom: 16px;
                    }
                    .text {
                      font-size: 16px;
                      color: #4b5563;
                      margin-bottom: 24px;
                    }
                    .code-container {
                      background-color: var(--slate-100);
                      border-radius: 6px;
                      padding: 16px;
                      text-align: center;
                      margin-bottom: 24px;
                      border: 1px solid var(--slate-200);
                    }
                    .verification-code {
                      font-family: 'Courier New', monospace;
                      font-size: 24px;
                      font-weight: 700;
                      letter-spacing: 4px;
                      color: var(--primary);
                    }
                    .button {
                      display: inline-block;
                      background-color: var(--primary);
                      color: white;
                      text-decoration: none;
                      padding: 12px 24px;
                      border-radius: 6px;
                      font-weight: 500;
                      margin-bottom: 24px;
                    }
                    .footer {
                      padding: 24px;
                      background-color: var(--slate-50);
                      text-align: center;
                      border-top: 1px solid var(--slate-200);
                    }
                    .footer-text {
                      font-size: 14px;
                      color: #6b7280;
                    }
                    .help-text {
                      font-size: 13px;
                      color: #9ca3af;
                      text-align: center;
                      margin-top: 8px;
                    }
                  </style>
                </head>
                <body>
                  <div style="padding: 20px;">
                    <div class="container">
                      <div class="header">
                        <h1 style="margin: 0; color: white; font-size: 24px; font-weight: 700; letter-spacing: 0.5px;">INeedHousing</h1>
                      </div>
                      <div class="content">
                        <h1 class="title">Verify Your Email Address</h1>
                        <p class="text">Thanks for signing up! Please enter the verification code below to complete your account setup.</p>
                        <div class="code-container">
                          <div class="verification-code">%s</div>
                        </div>
                        <p class="text">This code will expire in 10 minutes. If you didn't request this code, you can safely ignore this email.</p>
                        <p class="help-text">Having trouble? Contact our support team.</p>
                      </div>
                      <div class="footer">
                        <p class="footer-text">© 2025 INeedHousing. All rights reserved.</p>
                      </div>
                    </div>
                  </div>
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
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Verify Your Account</title>
                  <style>
                    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
                    :root {
                      --primary: #176087;
                      --foreground: #000000;
                      --slate-50: #f8fafc;
                      --slate-100: #f1f5f9;
                      --slate-200: #e2e8f0;
                    }
                    body {
                      font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                      margin: 0;
                      padding: 0;
                      line-height: 1.6;
                      background-color: #f5f5f5;
                    }
                    .container {
                      max-width: 600px;
                      margin: 0 auto;
                      background-color: #ffffff;
                      border-radius: 8px;
                      overflow: hidden;
                      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                      padding: 24px;
                      background-color: var(--primary);
                      text-align: center;
                    }
                    .header img {
                      height: 40px;
                    }
                    .content {
                      padding: 32px 24px;
                      background-color: #ffffff;
                    }
                    .title {
                      font-size: 20px;
                      font-weight: 600;
                      color: var(--foreground);
                      margin-top: 0;
                      margin-bottom: 16px;
                    }
                    .text {
                      font-size: 16px;
                      color: #4b5563;
                      margin-bottom: 24px;
                    }
                    .code-container {
                      background-color: var(--slate-100);
                      border-radius: 6px;
                      padding: 16px;
                      text-align: center;
                      margin-bottom: 24px;
                      border: 1px solid var(--slate-200);
                    }
                    .verification-code {
                      font-family: 'Courier New', monospace;
                      font-size: 24px;
                      font-weight: 700;
                      letter-spacing: 4px;
                      color: var(--primary);
                    }
                    .button {
                      display: inline-block;
                      background-color: var(--primary);
                      color: white;
                      text-decoration: none;
                      padding: 12px 24px;
                      border-radius: 6px;
                      font-weight: 500;
                      margin-bottom: 24px;
                    }
                    .footer {
                      padding: 24px;
                      background-color: var(--slate-50);
                      text-align: center;
                      border-top: 1px solid var(--slate-200);
                    }
                    .footer-text {
                      font-size: 14px;
                      color: #6b7280;
                    }
                    .help-text {
                      font-size: 13px;
                      color: #9ca3af;
                      text-align: center;
                      margin-top: 8px;
                    }
                  </style>
                </head>
                <body>
                  <div style="padding: 20px;">
                    <div class="container">
                      <div class="header">
                        <h1 style="margin: 0; color: white; font-size: 24px; font-weight: 700; letter-spacing: 0.5px;">INeedHousing</h1>
                      </div>
                      <div class="content">
                        <h1 class="title">Verify Your Email Address</h1>
                        <p class="text">Thanks for signing up! Please enter the verification code below to complete your account setup.</p>
                        <div class="code-container">
                          <div class="verification-code">%s</div>
                        </div>
                        <p class="text">This code will expire in 10 minutes. If you didn't request this code, you can safely ignore this email.</p>
                        <p class="help-text">Having trouble? Contact our support team.</p>
                      </div>
                      <div class="footer">
                        <p class="footer-text">© 2025 INeedHousing. All rights reserved.</p>
                      </div>
                    </div>
                  </div>
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
