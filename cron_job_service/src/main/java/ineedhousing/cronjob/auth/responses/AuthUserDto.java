package ineedhousing.cronjob.auth.responses;

import java.time.Instant;

public record AuthUserDto(String username, String token, Instant expirationTime) {
}
