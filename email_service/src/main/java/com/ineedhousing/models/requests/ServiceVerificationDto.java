package com.ineedhousing.models.requests;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public record ServiceVerificationDto(String apiToken, String serviceName, LocalDateTime timeStamp) {
}