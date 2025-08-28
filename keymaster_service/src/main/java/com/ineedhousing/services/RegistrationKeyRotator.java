package com.ineedhousing.services;

import com.ineedhousing.models.RotatingKeyEvent;
import com.ineedhousing.models.SuccessfulKeyRotationEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
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

    @PostConstruct
    void init(){
        rotateKey(new RotatingKeyEvent("Service booted up, initializing first key", LocalDateTime.now()));
    }

    public void rotateKey(@ObservesAsync RotatingKeyEvent event) {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[64];
        random.nextBytes(keyBytes);
        key =  Base64.getEncoder().encodeToString(keyBytes);
        publisher.fireAsync(new SuccessfulKeyRotationEvent("Key successfully created", LocalDateTime.now()));
    }

    public String getKey() {
        return key;
    }
}
