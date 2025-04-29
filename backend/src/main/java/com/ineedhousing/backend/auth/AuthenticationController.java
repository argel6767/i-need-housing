package com.ineedhousing.backend.auth;


import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.exceptions.UserAlreadyVerifiedException;
import com.ineedhousing.backend.auth.requests.*;
import com.ineedhousing.backend.auth.responses.LoginResponse;
import com.ineedhousing.backend.email.EmailVerificationException;
import com.ineedhousing.backend.email.InvalidEmailException;
import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.user.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * holds auth endpoints that can be accessed without a JWT token
 */
@RequestMapping("/auths")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    /**
     * register user endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticateUserDto request) {
        try {
            User registeredUser = authenticationService.signUp(request);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }
        catch (AuthenticationException ae) {
            return new ResponseEntity<>(ae.getMessage(), HttpStatus.CONFLICT);
        }
        catch (InvalidEmailException ive) {
            return new ResponseEntity<>(ive.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * login user endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticateUserDto request, HttpServletResponse response) {
        try {
            User user = authenticationService.authenticateUser(request);
            String token = jwtService.generateToken(user);
            String cookieHeader = jwtService.generateCookie(token, Optional.empty());
            response.setHeader("Set-Cookie", cookieHeader);
            // Return user info without token in body
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (EmailVerificationException eve) {
            return new ResponseEntity<>(eve.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InvalidEmailException ive) {
            return new ResponseEntity<>(ive.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * verify user endpoint via the code they input
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyUserDto request) {
        try {
            authenticationService.verifyUser(request);
            return ResponseEntity.ok("User verified!");
        }
        catch (UserAlreadyVerifiedException uave) {
            return ResponseEntity.ok("User already verified!");
        }
        catch (ExpiredVerificationCodeException eve) {
            return new ResponseEntity<>(eve.getMessage(), HttpStatus.GONE);
        }
        catch (RuntimeException re) {
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {   
        // Set cookie header with SameSite
        String cookieHeader = jwtService.generateCookie("", Optional.of(0L));
        response.setHeader("Set-Cookie", cookieHeader);
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * resend verification email endpoint
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody ResendEmailDto email) {
        try {
            authenticationService.resendVerificationEmail(email.getEmail());
            return ResponseEntity.ok("Verification code resent!");
        }
        catch (EmailVerificationException eve) {
            return ResponseEntity.badRequest().body(eve.getMessage());
        }
    }

    /**
     * changes passwords for user
     * THIS IS USED ONLY FOR WHEN A USER WANTS TO UPDATE PASSWORD
     */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto request) {
        try {
            return ResponseEntity.ok(authenticationService.changePassword(request));
        }
        catch (UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
        catch (RuntimeException re) {
            return new ResponseEntity<>(re.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * sends email for reset password request
     */
    @PostMapping("/forgot/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        try {
            authenticationService.sendForgottenPasswordVerificationCode(email);
            return ResponseEntity.ok("Forgot password verification code sent!");
        }
        catch (UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * resets password
     * ONLY TO BE USED FOR WHEN USER FORGETS PASSWORD
     */
    @PutMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordDto request) {
        try {
            User user = authenticationService.resetPassword(request);
            return ResponseEntity.ok(user);
        }
        catch (ExpiredVerificationCodeException re) {
            return new ResponseEntity<>(re.getMessage(), HttpStatus.GONE);
        }
        catch (AuthenticationException ae) {
            return new ResponseEntity<>(ae.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}
