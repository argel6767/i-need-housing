package com.ineedhousing.backend.auth;


import com.ineedhousing.backend.auth.exceptions.AuthenticationException;
import com.ineedhousing.backend.auth.exceptions.ExpiredVerificationCodeException;
import com.ineedhousing.backend.auth.exceptions.InvalidPasswordException;
import com.ineedhousing.backend.auth.exceptions.UserAlreadyVerifiedException;
import com.ineedhousing.backend.auth.requests.*;
import com.ineedhousing.backend.email.exceptions.EmailVerificationException;
import com.ineedhousing.backend.email.exceptions.InvalidEmailException;
import com.ineedhousing.backend.email.models.VerifyUserDto;
import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.jwt.JwtUtils;
import com.ineedhousing.backend.user.User;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * holds auth endpoints that can be accessed without a JWT token
 */
@Log
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
     * used to check if cookie is still valid, will return 200 if is
     * it won't even be run if it is not due the security chain and return a 403
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cookie-status")
    @RateLimiter(name = "auths")
    public ResponseEntity<String> checkCookie() {
        String email = JwtUtils.getCurrentUserEmail();
        log.info("Cookie is still valid for email: " + email);
        return new ResponseEntity<>("Token still valid", HttpStatus.OK);
    }

    /**
     * register user endpoint
     */
    @PostMapping("/register")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> register(@RequestBody AuthenticateUserDto request) {
        try {
            User registeredUser = authenticationService.signUp(request);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }
        catch (AuthenticationException ae) {
            return new ResponseEntity<>(ae.getMessage(), HttpStatus.CONFLICT);
        }
        catch (InvalidEmailException | InvalidPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (MessagingException me) {
            return ResponseEntity.internalServerError().body(me.getMessage());
        }
    }

    @PostMapping("/v2/register")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> registerV2(@RequestBody AuthenticateUserDto request) {
        try {
            User registeredUser = authenticationService.signUpV2(request);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }
        catch (AuthenticationException ae) {
            return new ResponseEntity<>(ae.getMessage(), HttpStatus.CONFLICT);
        }
        catch (InvalidEmailException | InvalidPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * login user endpoint
     */
    @PostMapping("/login")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> login(@RequestBody AuthenticateUserDto request, HttpServletResponse response) {
        try {
            User user = authenticationService.authenticateUser(request);
            String token = jwtService.generateToken(user);
            String cookieHeader = jwtService.generateCookie(token, Optional.empty(), "Strict");
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
    @RateLimiter(name = "auths")
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> logout(HttpServletResponse response) {   
        // Set cookie header with SameSite
        String cookieHeader = jwtService.generateCookie("", Optional.of(0L), "None");
        response.setHeader("Set-Cookie", cookieHeader);
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * resend verification email endpoint
     */
    @PostMapping("/resend")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> resend(@RequestBody ResendEmailDto email) {
        try {
            authenticationService.resendVerificationEmail(email.getEmail());
            return ResponseEntity.ok("Verification code resent!");
        }
        catch (EmailVerificationException eve) {
            return ResponseEntity.badRequest().body(eve.getMessage());
        }
        catch (MessagingException me) {
            return ResponseEntity.internalServerError().body(me.getMessage());
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/v2/resend")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> resendV2(@RequestBody ResendEmailDto email) {
        try {
            authenticationService.resendVerificationEmailV2(email.getEmail());
            return ResponseEntity.ok("Verification code resent!");
        }
        catch (EmailVerificationException eve) {
            return ResponseEntity.badRequest().body(eve.getMessage());
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * changes passwords for user
     * THIS IS USED ONLY FOR WHEN A USER WANTS TO UPDATE PASSWORD
     */
    @PutMapping("/password")
    @RateLimiter(name = "auths")
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
    @RateLimiter(name = "auths")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        try {
            authenticationService.sendForgottenPasswordVerificationCode(email);
            return ResponseEntity.ok("Forgot password verification code sent!");
        }
        catch (UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        } catch (MessagingException me) {
            return ResponseEntity.internalServerError().body(me.getMessage());
        }
    }

    /**
     * sends email for reset password request
     */
    @PostMapping("/v2/forgot/{email}")
    @RateLimiter(name = "auths")
    public ResponseEntity<?> forgotPasswordV2(@PathVariable String email) {
        try {
            authenticationService.sendForgottenPasswordVerificationCodeV2(email);
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
    @RateLimiter(name = "auths")
    public ResponseEntity<?> resetPasswordForgottenPassword(@RequestBody ForgotPasswordDto request) {
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
