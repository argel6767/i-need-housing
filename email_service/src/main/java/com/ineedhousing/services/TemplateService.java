package com.ineedhousing.services;

import com.ineedhousing.models.EmailTemplate;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import static com.ineedhousing.models.EmailTemplate.*;

@ApplicationScoped
public class TemplateService {

    @CacheResult(cacheName = "templates")
    public EmailTemplate getEmailTemplate(@CacheKey String templateName) {
        return findByName(templateName);
    }

    @CacheResult(cacheName = "templates")
    public List<EmailTemplate> getAllEmailTemplates() {
        return findAllTemplates();
    }

    @CacheInvalidateAll(cacheName = "templates")
    public EmailTemplate createEmailTemplate(String templateName, String templateContent) {
        return createTemplate(templateName, templateContent);
    }

    @CacheInvalidateAll(cacheName = "templates")
    public EmailTemplate updateEmailTemplate(@CacheKey String templateName, String templateContent) {
        return updateTemplate(templateName, templateContent);
    }

    @CacheInvalidateAll(cacheName = "templates")
    public void deleteEmailTemplate(@CacheKey String templateName) {
        deleteByName(templateName);
    }
}
