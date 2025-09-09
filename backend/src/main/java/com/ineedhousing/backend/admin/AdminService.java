package com.ineedhousing.backend.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import com.ineedhousing.backend.admin.models.AuthenticatedAdminDto;
import com.ineedhousing.backend.user.responses.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.admin.exceptions.UnauthorizedAccessException;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;
import com.ineedhousing.backend.email.exceptions.EmailVerificationException;
import com.ineedhousing.backend.email.exceptions.InvalidEmailException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;
import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;

@Service
@Lazy
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final HousingListingRepository housingListingRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AdminService(UserRepository userRepository, HousingListingRepository housingListingRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.housingListingRepository = housingListingRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticatedAdminDto authenticateAdmin(AuthenticateUserDto request)  {
        if (!isValidEmail(request.getUsername())) {
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (!user.isEnabled()) {
            throw new EmailVerificationException("Email not verified");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(), user.getAuthorities()));
        if (!isUserAdmin(user)) {
            throw new UnauthorizedAccessException(request.getUsername() + " lacks necessary privileges");
        }
        user.setLastLogin(LocalDateTime.now());
        String token = jwtService.generateToken(user);
        String cookie = jwtService.generateCookie(token, Optional.empty());
        userRepository.save(user);
        UserDto dto = createUserDto(user);
        log.info("Signed in user: {}", dto);
        return new AuthenticatedAdminDto(cookie, dto);
    }

    private boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private boolean isUserAdmin(User user) {
        return user.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private UserDto createUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getAuthorities(), user.getLastLogin(), user.getCreatedAt());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<HousingListing> getAllListings() {
        return housingListingRepository.findAll();
    }
    
}
