package com.ineedhousing.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;

/**
 * Houses endpoints for User interactions
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * getUser via their email
     * @param email
     * @return ResponseEntity
     */
    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates User with whatever new values given
     * @param user
     * @param email
     * @return USer
     */
    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String email) {
        try {
            User updatedUser = userService.updateUser(user, email);
            return ResponseEntity.ok(updatedUser);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * specifically only updates the user type
     * @param request
     * @return ResponseEntity
     */
    @PutMapping("/type")
    public ResponseEntity<?> setUserType(@RequestBody SetUserTypeRequest request) {
        if (request.getUserType() == null) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.setUserType(request);
            return ResponseEntity.ok(user);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * deletes User
     * @param email
     * @return ResponseEntity
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        try {
            String response = userService.deleteUser(email);
            return ResponseEntity.ok(response);
        }
        catch(UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
