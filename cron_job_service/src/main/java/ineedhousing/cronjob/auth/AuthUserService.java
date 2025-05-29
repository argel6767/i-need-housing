package ineedhousing.cronjob.auth;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ineedhousing.cronjob.auth.requests.LoginDto;
import ineedhousing.cronjob.auth.requests.RegistrationDto;
import ineedhousing.cronjob.auth.responses.AuthUserDto;
import ineedhousing.cronjob.jwt.JwtService;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;

@Service
public class AuthUserService {

    @Value("${registration.secret.key}")
    private String secretKey;
    private final JwtService jwtService;

    public AuthUserService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Registers a new service to be able to use the cron job service
     * they must have the register key before hand otherwise they cannot register
     * 
     * @param request
     * @return
     */
    @Transactional
    public String register(RegistrationDto request) {
        if (!(request.registrationKey().equals(secretKey))) {
            Log.warn("Registration key is incorrect");
            return "Registration key is incorrect";
        }
        AuthUser conflictUser = AuthUser.findByUsername(request.username());
        if (conflictUser != null) {
            Log.warn("Username is already taken, choose another");
            return "Username is already taken, choose another";
        }
        AuthUser user = new AuthUser();
        user.username = request.username();
        user.password = BcryptUtil.bcryptHash(request.password());
        user.lastLoggedIn = LocalDateTime.now();
        user.persist();
        Log.warn("User created with username: " + request.username());
        return "User created with username: " + request.username();
    }

    /**
     * Logs in a user to the service
     * 
     * @param request
     * @return
     */
    @Transactional
    public AuthUserDto login(LoginDto request) {
        Log.info("Attempting to log in user: " + request.username());
        AuthUser user = AuthUser.findByUsernameAndPassword(request.username(), request.password());
        if (user == null) {
            Log.warn("No user found for given credentials");
            return null;
        }
        Log.info("Creating token");
        String token = jwtService.createToken(user.id, user.username);
        Instant expiresAt = Instant.now().plusSeconds(1800);
        Log.info("User logged in");
        return new AuthUserDto(user.username, token, expiresAt);
    }

}
