package com.ineedhousing.services;

import com.ineedhousing.models.EmailTemplate;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import static com.ineedhousing.models.EmailTemplate.*;

@ApplicationScoped
public class TemplateService {

    public EmailTemplate getEmailTemplate(String templateName) {
        return findByName(templateName);
    }

    public List<EmailTemplate> getAllEmailTemplates() {
        return findAllTemplates();
    }

    public EmailTemplate createEmailTemplate(String templateName, String templateContent) {
        return createTemplate(templateName, templateContent);
    }

    public EmailTemplate updateEmailTemplate(String templateName, String templateContent) {
        return updateTemplate(templateName, templateContent);
    }

    public void deleteEmailTemplate(String templateName) {
        deleteByName(templateName);
    }
}
