package com.ineedhousing.backend.jwt;

import com.ineedhousing.backend.user.User;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    /**
     * Gets the email of the currently authenticated user from the JWT token
     * @return The email of the authenticated user
     * @throws IllegalStateException if no user is authenticated
     */
    public static String getCurrentUserEmail() {
       return getCurrentUser().getUsername();
    }

    /**
     * Gets the Id of the currently authenticated user
     * by casting the UserDetails object to User entity class
     * @return long
     */
    public static Long getCurrentUserId() {
        User user = (User) getCurrentUser();
       return user.getId();
    }

    /**
     * Determines whether the currently authenticated user is a admin
     * @return whether a user has an admin role
     * @throws IllegalStateException if no user is authenticated or the use has no roles
     */
    public static boolean isUserAdmin() {
        UserDetails userDetails = getCurrentUser();
        boolean isAdmin = (userDetails).getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin;
    }

    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authentication found");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new InsufficientAuthenticationException("Currently authenticated principal is not a user. They could be a service instead");
        }

        return (UserDetails) principal;
    }
}

