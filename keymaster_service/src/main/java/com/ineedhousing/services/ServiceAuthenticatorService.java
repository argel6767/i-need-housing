package com.ineedhousing.services;

import com.ineedhousing.models.dtos.RegisteredServiceDto;
import com.ineedhousing.models.dtos.RegistrationDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

@ApplicationScoped
public class ServiceAuthenticatorService {

    @Inject
    RegistrationKeyRotator rotator;

    @Inject
    DataSource dataSource;

    @Inject
    ApiTokenGenerator apiTokenGenerator;

    @Inject
    TokenHasher tokenHasher;

    public RegisteredServiceDto registerService(RegistrationDto registrationDto) {
        String registrationKey = rotator.getKey();
        if (registrationKey == null) {
            throw new IllegalStateException("Registration key has not been initialized");
        }
        if (areAnyValuesNull(registrationDto)) {
            throw new BadRequestException("All values are mandatory");
        }
        if (!registrationDto.registrationKey().equals(registrationKey)) {
            throw new SecurityException("Registration key is wrong");
        }
        if (isServicePresent(registrationDto.serviceName())) {
            throw new SecurityException("Service name is already in use");
        }

        String apiToken = apiTokenGenerator.generateApiToken(registrationDto.registrationKey());
        String tokenHash = hashToken(apiToken);


        String sqlStatement = "INSERT INTO registered_service (id, service_name, api_token_hash, created_date) VALUES (nextval('registered_service_seq'), ?, ?, ?)";
        try (Connection preparedStatement = dataSource.getConnection()) {
            PreparedStatement statement = preparedStatement.prepareStatement(sqlStatement);
            statement.setString(1, registrationDto.serviceName());
            statement.setString(2, tokenHash);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register new service", e);
        }

        return new RegisteredServiceDto(String.format("%s service successfully registered", registrationDto.serviceName()), apiToken, registrationDto.serviceName(), LocalDateTime.now());
    }

    private boolean areAnyValuesNull(RegistrationDto registrationDto) {
        return registrationDto == null || registrationDto.serviceName() == null || registrationDto.registrationKey() == null;
    }

    private boolean isServicePresent(String serviceName) {
        String sqlStatement = "SELECT * FROM registered_service WHERE service_name = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, serviceName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e) {
            throw new RuntimeException("Unable to handle SQL request", e);
        }
    }

    private String hashToken(String apiToken) {
        try {
            return tokenHasher.hashToken(apiToken);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to hash API Token", e);
        }
    }
}
