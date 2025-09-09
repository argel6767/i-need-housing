package com.ineedhousing.models.requests;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record TemplateDto(String templateName, String templateContent) {
}
