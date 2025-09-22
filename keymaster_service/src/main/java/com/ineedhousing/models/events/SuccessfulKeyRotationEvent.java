package com.ineedhousing.models.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection(serialization = true)
public record SuccessfulKeyRotationEvent(String message, String newKey, LocalDateTime timeStamp) {
}
