package ineedhousing.cronjob.auth;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class ApiTokenValidationService {

    @Inject
    DataSource dataSource;

    @Inject
    LogService logService;

    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);


    public boolean isServiceAuthenticated(String token, String serviceName) {
        Optional<String> tokenHash = getApiTokenHash(serviceName);

        if (!tokenHash.isPresent()) {
            return false;
        }
        return isApiTokenCorrect(token, tokenHash.get());
    }

    private Optional<String> getApiTokenHash(String serviceName) {
        String sql = "SELECT api_token_hash FROM registered_service WHERE service_name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, serviceName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("api_token_hash"));
                }
            }

        } catch (SQLException e) {
            logService.publish("Failed to fetch API token for service " + serviceName, LoggingLevel.ERROR);
        }
        return Optional.empty();
    }

    private boolean isApiTokenCorrect(String token, String tokenHash) {
        try {
            return argon2.verify(token, tokenHash.toCharArray());
        }
        catch (Exception e) {
            logService.publish("Failed to verify API token for service " + token, LoggingLevel.ERROR);
        }
        return false;
    }
}
