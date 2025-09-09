package com.ineedhousing.backend.email.models;

import java.time.LocalDateTime;

public record EmailTemplate(String templateName, String templateContent, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
