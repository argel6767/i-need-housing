import {EmailTemplate, TemplateDto} from "@/lib/models";
import {apiClient} from "@/api/apiConfig";

export const createEmailTemplate = async (template: TemplateDto): Promise<EmailTemplate> => {
    try {
        const response = await apiClient.post("/admin/email-service/templates", template);
        return response.data;
    }
    catch (error) {
        console.error(error);
        return {id: -1, templateContent: `Could not create new template ${template.templateName} try again later`, templateName:template.templateName, createdAt: "Failed to create new template", updatedAt: ""};
    }
}

export const getEmailTemplate = async (templateName: string): Promise<EmailTemplate | null> => {
    try {
        const response = await apiClient.get(`/admin/email-service/templates/${templateName}`);
        return response.data;
    }
    catch (error) {
        console.log(error);
        return null;
    }
}

export const updateEmailTemplate = async (template: TemplateDto): Promise<EmailTemplate> => {
    try {
        const response = await apiClient.put("/admin/email-service/templates", template);
        return response.data;
    }
    catch (error) {
        console.error(error);
        return {id: -1, templateContent: `Could not create new template ${template.templateName} try again later`, templateName:template.templateName, createdAt: "Failed to create new template", updatedAt: ""};
    }
}

export const generateEmailTemplate = async (prompt: string): Promise<string> => {
    try {
        const response = await apiClient.post("/admin/templates/ai", prompt);
        return response.data;
    }
    catch (error) {
        console.error(error);
        return `<p style="color: red">Could not create new template try again later</p><p style="color: red">Prompt given: ${prompt}</p>`;
    }
}