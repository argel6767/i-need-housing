package com.ineedhousing.services;

import com.ineedhousing.models.dtos.RegistrationKeyDto;
import com.ineedhousing.models.enums.LoggingLevel;
import com.ineedhousing.models.events.RotatingKeyEvent;
import com.ineedhousing.models.events.SuccessfulKeyRotationEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;

import java.security.SecureRandom;

import java.time.LocalDateTime;
import java.util.Base64;

@ApplicationScoped
public class RegistrationKeyRotator {

    private String key;

    @Inject
    Config config;

    private String mainApiServiceName;

    @PostConstruct
    void init() {
        mainApiServiceName = config.getOptionalValue("ineedhousing.service.name", String.class)
                .orElseThrow(() -> new IllegalStateException("INeedHousing service name not found"));
    }

    @Inject
    Event<SuccessfulKeyRotationEvent> publisher;

    @Inject
    LogService log;

    public void onStartup(@Observes StartupEvent ev) {
        log.publish("Service Initialized, setting first key", LoggingLevel.INFO);
        key = generateKey();
        log.publish("Registration key successfully generated at startup. Keymaster ready to register new services", LoggingLevel.INFO);
    }

    public void rotateKey(@ObservesAsync RotatingKeyEvent event) {
        key = generateKey();
        log.publish("Key successfully rotated", LoggingLevel.INFO);
    }

    private String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[64];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public String getKey() {
        if (key == null) {
            log.publish("Key should not be null, generating emergency key.", LoggingLevel.WARN);
            key = generateKey();
        }
        return key;
    }

    public RegistrationKeyDto getRegistrationKey(String serviceName) {
        if (!serviceName.equals(mainApiServiceName)) {
            log.publish("Registration key can only be accessed by INeedHousing API only", LoggingLevel.INFO);
            throw new SecurityException("Registration key can only be accessed by INeedHousing API only");
        }
        return new RegistrationKeyDto(key, LocalDateTime.now());
    }
}
