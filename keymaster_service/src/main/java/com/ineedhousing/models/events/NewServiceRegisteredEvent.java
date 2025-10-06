package com.ineedhousing.models.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public record NewServiceRegisteredEvent(String serviceName, String message, LocalDateTime timestamp) {
}
