package ineedhousing.cronjob.auth.responses;

import java.time.LocalDateTime;

public record AuthUserDto(String username, String token, LocalDateTime expirationTime) {
}
