package com.ineedhousing.backend.user;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.jwt.JwtUtils;
import com.ineedhousing.backend.user.requests.SetUserTypeRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Houses endpoints for User interactions
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the current authenticated user
     * @return ResponseEntity
     */
    @GetMapping("/me")
    @RateLimiter(name = "user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Updates the current authenticated user with new values
     * @param user
     * @return User
     */
    @PutMapping("/me")
    @RateLimiter(name = "user")
    public ResponseEntity<?> updateCurrentUser(@RequestBody User user) {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            User updatedUser = userService.updateUser(user, email);
            return ResponseEntity.ok(updatedUser);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * specifically only updates the user type
     * @param request
     * @return ResponseEntity
     */
    @PutMapping("/type")
    @RateLimiter(name = "user")
    public ResponseEntity<?> setUserType(@RequestBody SetUserTypeRequest request) {
        log.info("user type: " + request.toString());
        if (request.getUserType() == null || request.getUserType().toString().isEmpty()) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        try {
            String email = JwtUtils.getCurrentUserEmail();
            User user = userService.setUserType(request, email);
            return ResponseEntity.ok(user);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * deletes the current authenticated user
     * @return ResponseEntity
     */
    @DeleteMapping("/me")
    @RateLimiter(name = "user")
    public ResponseEntity<?> deleteCurrentUser() {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            String response = userService.deleteUser(email);
            return ResponseEntity.ok(response);
        }
        catch(UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
