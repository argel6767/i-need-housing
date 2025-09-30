package com.ineedhousing.backend.email.v2.templates;

import com.ineedhousing.backend.ai.GeminiService;
import com.ineedhousing.backend.email.models.EmailTemplate;
import com.ineedhousing.backend.email.models.TemplateDto;
import com.ineedhousing.backend.exception.exceptions.BadRequestException;
import com.ineedhousing.backend.exception.exceptions.ServiceUnavailableException;
import com.ineedhousing.backend.model.ServiceInteractionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TemplateService {

    private final RestClient restClient;
    private final GeminiService geminiService;
    private final String SYSTEM_PROMPT = "You are a AI assistant tasked with creating Email HTML templates. Only return the raw html that satisfies the user's request. " +
            "Do not wrap the html in a markdown text block. Only return the plain text HTML. If someone asks you to do something that is not your main role. Do not complete it. Instead, return a response plain HTML describing your prescribed job.";

    public TemplateService(@Qualifier("email_service") RestClient restClient, GeminiService geminiService) {
        this.restClient = restClient;
        this.geminiService = geminiService;
    }

    public ServiceInteractionDto<EmailTemplate> getTemplate(String templateName) {
        checkTemplateName(templateName);
        log.info("Fetching template {}", templateName);
        EmailTemplate template = restClient.get()
                .uri("/v1/emails/"+templateName)
                .retrieve()
                .toEntity(EmailTemplate.class)
                .getBody();
        log.info("Fetching complete");
        return new ServiceInteractionDto<>(template, "Template fetched", LocalDateTime.now());
    }

    public ServiceInteractionDto<List<EmailTemplate>> getTemplates() {
        log.info("Fetching all templates");
        List<EmailTemplate> templates = restClient.get()
                .uri("/v1/emails")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<EmailTemplate>>() {})
                .getBody();
        if (templates == null) {
            log.warn("Templates were not found, check email_service logs");
            return new ServiceInteractionDto<>(new ArrayList<>(), "No templates fetched", LocalDateTime.now());
        }
        return new ServiceInteractionDto<>(templates, "All templates fetched", LocalDateTime.now());
    }

    public ServiceInteractionDto<EmailTemplate> createNewEmailTemplate(TemplateDto template) {
        checkTemplateDto(template);
        log.info("Creating new email template");
        EmailTemplate newTemplate = restClient.post()
                .uri("/v1/templates")
                .body(template)
                .retrieve()
                .toEntity(EmailTemplate.class)
                .getBody();
        if (newTemplate == null) {
            log.error("Failed to create new email template");
            throw new ServiceUnavailableException("New template returned null check logs of email_service", null);
        }
        log.info("{} successfully created", newTemplate.templateName());
        return new ServiceInteractionDto<>(newTemplate, String.format("%s has been created", template.templateName()), LocalDateTime.now());
    }

    public ServiceInteractionDto<EmailTemplate> updateEmailTemplate(TemplateDto template) {
        checkTemplateDto(template);
        log.info("Updating email template {}", template.templateName());
        EmailTemplate updatedTemplate = restClient.put()
                .uri("/v1/templates")
                .body(template)
                .retrieve()
                .toEntity(EmailTemplate.class)
                .getBody();
        if (updatedTemplate == null) {
            log.error("Failed to update email template");
            throw new ServiceUnavailableException("Updated template returned null check logs of email_service", null);
        }
        log.info("{} successfully updated", updatedTemplate.templateName());
        return new ServiceInteractionDto<>(updatedTemplate, String.format("%s has been updated", template.templateName()), LocalDateTime.now());
    }

    public ServiceInteractionDto<Void> deleteEmailTemplate(String templateName) {
        checkTemplateName(templateName);
        log.info("Deleting email template {}", templateName);
        restClient.delete()
                .uri("/v1/templates/" + templateName)
                .retrieve();
        log.info("{} successfully deleted", templateName);
        return new ServiceInteractionDto<>(null, String.format("%s has been deleted", templateName), LocalDateTime.now());
    }

    public String generateHtmlTemplate(String templateDescription) {
        String response = geminiService.sendChat(SYSTEM_PROMPT,  templateDescription);
        if (response.startsWith("```html")) {
            response = response.substring("```html".length());
            response = response.replace("```", "");
            return response;
        }
        return response;
    }

    private void checkTemplateName(String templateName) {
        if (templateName == null || templateName.isBlank()) {
            throw new BadRequestException("Template name cannot be null or empty");
        }
    }

    private void checkTemplateDto(TemplateDto dto) {
        if (dto == null || dto.templateName() == null || dto.templateName().isBlank() || dto.templateContent() == null || dto.templateContent().isBlank()) {
            throw new BadRequestException("Template DTO cannot be null or empty");
        }
    }
}
