package com.ineedhousing.resources;

import com.ineedhousing.models.EmailTemplate;
import com.ineedhousing.models.requests.TemplateDto;
import com.ineedhousing.services.TemplateService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/v1/templates")
public class TemplateResource {

    @Inject
    TemplateService templateService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response create(TemplateDto newTemplateDto) {
        EmailTemplate newTemplate = templateService.createEmailTemplate(newTemplateDto.templateName(), newTemplateDto.templateContent());
        return Response.status(Response.Status.CREATED).entity(newTemplate).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10)
    public List<EmailTemplate> getAllEmailTemplates() {
        return templateService.getAllEmailTemplates();
    }

    @Path("/{templateName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10)
    public EmailTemplate getTemplate(@PathParam("templateName") String templateName) {
        return templateService.getEmailTemplate(templateName);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10, windowUnit = ChronoUnit.MINUTES)
    public EmailTemplate updateTemplate(TemplateDto template) {
        return templateService.updateEmailTemplate(template.templateName(), template.templateContent());
    }

    @Path("/{templateName}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RateLimit(value = 5, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response deleteTemplate(@PathParam("templateName") String templateName) {
        templateService.deleteEmailTemplate(templateName);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
