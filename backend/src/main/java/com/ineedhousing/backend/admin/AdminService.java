package com.ineedhousing.backend.admin;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.admin.exceptions.UnauthorizedAccessException;
import com.ineedhousing.backend.auth.requests.AuthenticateUserDto;
import com.ineedhousing.backend.auth.responses.LoginResponse;
import com.ineedhousing.backend.email.EmailVerificationException;
import com.ineedhousing.backend.email.InvalidEmailException;
import com.ineedhousing.backend.jwt.JwtService;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AdminService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse authenticateAdmin(AuthenticateUserDto request)  {
        if (!isValidEmail(request.getUsername())) {
            throw new InvalidEmailException(request.getUsername() + " is an invalid email");
        }
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
        if (!user.isEnabled()) {
            throw new EmailVerificationException("Email not verified");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(), user.getAuthorities()));
        if (isUserAdmin(user)) {
            throw new UnauthorizedAccessException(request.getUsername() + " lacks necessary privileges");
        }
        user.setLastLogin(LocalDateTime.now());
        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse(token, jwtService.getExpirationTime());
        userRepository.save(user);
        return response;
    }

    private boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private boolean isUserAdmin(User user) {
        return user.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

}
