package com.ineedhousing.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class EmailTemplate extends PanacheEntity {
    public String templateName;
    @Column(name = "template_content", columnDefinition = "TEXT")
    public String templateContent;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    // Auto-set timestamps
    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static EmailTemplate findByName(String name) {
        return find("templateName", name).firstResult();
    }

    public static List<EmailTemplate> findAllTemplates() {
        return findAll().list();
    }

    @Transactional
    public static EmailTemplate createTemplate(String templateName, String templateContent) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.templateName = templateName;
        emailTemplate.templateContent = templateContent;
        emailTemplate.persist();
        return emailTemplate;
    }

    @Transactional
    public static EmailTemplate updateTemplate(String templateName, String templateContent) {
        EmailTemplate emailTemplate = findByName(templateName);
        emailTemplate.templateContent = templateContent;
        return emailTemplate;
    }

    @Transactional
    public static void deleteByName(String name) {
        delete("templateName", name);
    }
}
