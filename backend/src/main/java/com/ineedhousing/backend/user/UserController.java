package com.ineedhousing.backend.user;

import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.user.responses.UserDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.util.Optional;

/**
 * Houses endpoints for User interactions
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
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
            UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getAuthorities(),  user.getLastLogin(), user.getCreatedAt());
            return ResponseEntity.ok(userDto);
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

    @PutMapping("/v2/me")
    @RateLimiter(name = "user")
    public ResponseEntity<?> updateCurrentUser(@RequestBody UserDto userDto) {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            UserDto updatedUser = userService.updateUser(userDto, email);
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
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    @RateLimiter(name = "user")
    public ResponseEntity<?> deleteCurrentUser(HttpServletResponse response) {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            String responseMessage = userService.deleteUser(email);

            String cookieHeader = jwtService.generateCookie("",0L, "None");
            response.setHeader("Set-Cookie", cookieHeader);
            return ResponseEntity.ok(responseMessage);
        }
        catch(UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
