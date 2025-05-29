package ineedhousing.cronjob.auth;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import ineedhousing.cronjob.auth.requests.RegistrationDto;
import io.quarkus.elytron.security.common.BcryptUtil;


public class AuthUserService {

    @Value("${registration.secret.key}")
    private String secretKey;

    /**
     * Registers a new service to be able to use the cron job service
     * they must have the register key before hand otherwise they cannot register
     * @param request
     * @return
     */
    public String register(RegistrationDto request) {
        if (!(request.registrationKey().equals(secretKey))) {
            throw new SecurityException("request key is incorrect");
        }
        AuthUser user = new AuthUser();
        user.username = request.username();
        user.password = BcryptUtil.bcryptHash(request.password());
        user.lastLoggedIn = LocalDateTime.now();
        user.persist();
        return "User created with username: " + request.username();
    }


}
