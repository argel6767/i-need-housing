package ineedhousing.cronjob.jwt;

import java.time.Instant;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {

    public String createToken(Long userId, String username) {
        return Jwt.issuer("i-need-housing/cron-job-service")
        .subject(String.valueOf(userId))
        .upn(username)
        .claim("username", username)
        .expiresAt(Instant.now().plusSeconds(1800))
        .issuedAt(Instant.now())
        .sign();
    }
}
