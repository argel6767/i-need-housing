package com.ineedhousing.services;

import com.ineedhousing.models.dtos.RegistrationKeyDto;
import com.ineedhousing.models.events.RotatingKeyEvent;
import com.ineedhousing.models.events.SuccessfulKeyRotationEvent;
import io.quarkus.logging.Log;
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

    public void onStartup(@Observes StartupEvent ev) {
        Log.info("Service Initialized, setting first key");
        key = generateKey();
        Log.info("Registration key successfully generated at startup, firing notify event");
        publisher.fireAsync(new SuccessfulKeyRotationEvent("Key Successfully Rotated", key, LocalDateTime.now()));
    }

    public void rotateKey(@ObservesAsync RotatingKeyEvent event) {
        key = generateKey();
        Log.info("Key successfully rotated, firing notify event");
        publisher.fireAsync(new SuccessfulKeyRotationEvent("Key Successfully Rotated", key, LocalDateTime.now()));
    }

    private String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[64];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public String getKey() {
        if (key == null) {
            Log.warn("Key should not be null, generating emergency key.");
            key = generateKey();
        }
        return key;
    }

    public RegistrationKeyDto getRegistrationKey(String serviceName) {
        if (!serviceName.equals(mainApiServiceName)) {
            Log.warn("Registration key can only be accessed by INeedHousing API only");
            throw new SecurityException("Registration key can only be accessed by INeedHousing API only");
        }
        return new RegistrationKeyDto(key, LocalDateTime.now());
    }
}
