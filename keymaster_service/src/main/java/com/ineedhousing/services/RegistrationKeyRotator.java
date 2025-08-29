package com.ineedhousing.services;

import com.ineedhousing.models.RotatingKeyEvent;
import com.ineedhousing.models.SuccessfulKeyRotationEvent;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

import java.security.SecureRandom;

import java.time.LocalDateTime;
import java.util.Base64;

@ApplicationScoped
public class RegistrationKeyRotator {

    private String key;

    @Inject
    Event<SuccessfulKeyRotationEvent> publisher;

    public void onStartup(@Observes StartupEvent ev) {
        Log.info("Service Initialized, setting first key");
        key = generateKey();
        Log.info("Registration key successfully generated at startup, firing notify event");
        Log.warn(String.format("Key: %s. REMOVE THIS LOG ONCE KEYMASTER SERVICE IS REGISTERED", key));
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
}
