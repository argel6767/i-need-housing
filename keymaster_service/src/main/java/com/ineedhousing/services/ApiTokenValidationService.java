package com.ineedhousing.services;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.models.dtos.ServiceVerificationDto;
import com.ineedhousing.models.dtos.VerifiedServiceDto;
import com.ineedhousing.models.enums.LoggingLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class ApiTokenValidationService {

    @Inject
    DataSource dataSource;

    @Inject
    TokenHasher tokenHasher;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    LogService log;

    public VerifiedServiceDto isServiceAuthenticated(String serviceVerificationDto, String requestingService) {
        ServiceVerificationDto dtoParsed = parseRequestBody(serviceVerificationDto);
        log.publish(String.format("Checking authorization status of %s for %s", dtoParsed.serviceName(), requestingService), LoggingLevel.INFO);
        if (!isServiceAuthorized(dtoParsed.apiToken(), dtoParsed.serviceName())) {
            return new VerifiedServiceDto("Service is not authorized", LocalDateTime.now());
        }
        return new VerifiedServiceDto("Service is authorized", LocalDateTime.now());
    }

    private ServiceVerificationDto parseRequestBody(String serviceVerificationDto) {
        try {
            return objectMapper.readValue(serviceVerificationDto, ServiceVerificationDto.class);
        } catch (JacksonException e) {
            throw new BadRequestException("Could not parse request body, " + e.getMessage());
        }
    }

    public boolean isServiceAuthorized(String token, String serviceName) {
        log.publish("Verifying authorization status for " + serviceName, LoggingLevel.INFO);
        Optional<String> tokenHash = getApiTokenHash(serviceName);

        if (!tokenHash.isPresent()) {
            log.publish("No API token has been found for " + serviceName, LoggingLevel.INFO);
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
            log.publish("Failed to fetch api token hash from Database. Returning empty as default. Error Message: " +  e.getMessage(), LoggingLevel.ERROR);
        }
        return Optional.empty();
    }

    private boolean isApiTokenCorrect(String token, String tokenHash) {
        try {
            return tokenHasher.matches(token, tokenHash);
        }
        catch (Exception e) {
            log.publish("Failed to verify api token. Error Message: " +  e.getMessage(), LoggingLevel.ERROR);
        }
        return false;
    }
}

