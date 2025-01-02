package com.ineedhousing.backend.user;

import com.ineedhousing.backend.user.requests.SetUserTypeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
