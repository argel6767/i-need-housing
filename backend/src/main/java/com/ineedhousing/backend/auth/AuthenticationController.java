package com.ineedhousing.backend.auth;


import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.requests.*;
import com.ineedhousing.backend.auth.responses.LoginResponse;
import com.ineedhousing.backend.email.EmailVerificationException;
import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * holds auth endpoints that can be accessed without a JWT token
 */
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    /*
     * register user endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthenticateUserDto request) {
        try {
            User registeredUser = authenticationService.signUp(request);
            return ResponseEntity.ok(registeredUser);
        }
        catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    /*
     * login user endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticateUserDto request) {
        try {
            User user = authenticationService.authenticateUser(request);
            String token = jwtService.generateToken(user);
            LoginResponse response = new LoginResponse(token, jwtService.getExpirationTime());
            return ResponseEntity.ok(response);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        catch (EmailVerificationException eve) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    /*
     * verify user endpoint via the code they input
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyUserDto request) {
        try {
            authenticationService.verifyUser(request);
            return ResponseEntity.ok("User verified!");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * resend verification email endpoint
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody ResendEmailDto email) {
        try {
            authenticationService.resendVerificationEmail(email.getEmail());
            return ResponseEntity.ok("Verification code resent!");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
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

    /*
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
