package com.ineedhousing.models.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public record VerifiedServiceDto(String authorizedStatus, LocalDateTime timeStamp) {
}
