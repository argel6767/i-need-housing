package com.ineedhousing.backend.email.v2.templates;

import com.ineedhousing.backend.email.models.EmailTemplate;
import com.ineedhousing.backend.email.models.TemplateDto;
import com.ineedhousing.backend.model.ServiceInteractionDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/templates/{templateName}")
    public ServiceInteractionDto<EmailTemplate> getTemplate(@PathVariable String templateName) {
        return templateService.getTemplate(templateName);
    }

    @GetMapping("/templates")
    public ServiceInteractionDto<List<EmailTemplate>> getTemplates() {
        return templateService.getTemplates();
    }

    @PostMapping("/templates")
    public ServiceInteractionDto<EmailTemplate> createTemplate(@RequestBody TemplateDto dto) {
        return templateService.createNewEmailTemplate(dto);
    }

    @PutMapping("/templates")
    public ServiceInteractionDto<EmailTemplate> updateTemplate(@RequestBody TemplateDto dto) {
        return templateService.updateEmailTemplate(dto);
    }

    @DeleteMapping("/templates/{templateName}")
    public ServiceInteractionDto<Void> deleteTemplate(@PathVariable String templateName) {
        return templateService.deleteEmailTemplate(templateName);
    }
}
