package com.ineedhousing.services;

import com.ineedhousing.models.ServiceVerificationDto;
import com.ineedhousing.models.VerifiedServiceDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import io.quarkus.logging.Log;

@ApplicationScoped
public class ApiTokenValidationService {

    @Inject
    DataSource dataSource;

    @Inject
    TokenHasher tokenHasher;

    public VerifiedServiceDto isServiceAuthenticated(ServiceVerificationDto serviceVerificationDto) {
        if (!isServiceAuthenticated(serviceVerificationDto.apiToken(), serviceVerificationDto.serviceName())) {
            return new VerifiedServiceDto("Service is not authorized", LocalDateTime.now());
        }
        return new VerifiedServiceDto("Service is authorized", LocalDateTime.now());
    }

    public boolean isServiceAuthenticated(String token, String serviceName) {
        Optional<String> tokenHash = getApiTokenHash(serviceName);

        if (!tokenHash.isPresent()) {
            Log.warn("No API token has been found for " + serviceName);
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
            Log.error("Failed to fetch api token hash from Database", e);
        }
        return Optional.empty();
    }

    private boolean isApiTokenCorrect(String token, String tokenHash) {
        try {
            return tokenHasher.matches(token, tokenHash);
        }
        catch (Exception e) {
            Log.error("Failed to verify api token", e);
        }
        return false;
    }
}

