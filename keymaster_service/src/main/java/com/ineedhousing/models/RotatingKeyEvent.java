package com.ineedhousing.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public record RotatingKeyEvent(String message, LocalDateTime timeStamp) {
}
