package com.ineedhousing.services;

import com.ineedhousing.models.EmailTemplate;
import com.ineedhousing.models.requests.KeyRotationEvent;
import com.ineedhousing.models.requests.NewListingsMadeEvent;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ineedhousing.constants.TemplateNames.NEW_LISTINGS_CREATED;
import static com.ineedhousing.constants.TemplateNames.REGISTER_KEY_ROTATION;

@ApplicationScoped
public class ServiceInteractionEmailService {

    private final int MAX_RETRIES = 10;

    @Inject
    Mailer mailer;

    @Inject
    EntityManager entityManager;

    @Inject
    TemplateService templateService;

    @Inject
    ExecutorService virtualThreadExecutor;

    public void sendEmail(String to, String subject, String body) {
        Mail mail = Mail.withHtml(to, subject, body);
        mailer.send(mail);
    }

    public void sendKeyRotationEmail(KeyRotationEvent keyRotationEvent) {
        Log.info("Sending key rotation email to admins");
        EmailTemplate emailTemplate = templateService.getEmailTemplate(REGISTER_KEY_ROTATION);
        String body = String.format(emailTemplate.templateContent, keyRotationEvent.newKey());
        sendMailToAdmins(keyRotationEvent.message(), body);
    }

    public void sendNewListingsEmail(NewListingsMadeEvent newListingsMadeEvent) {
        Log.info("Sending new listings made email to admins");
        EmailTemplate emailTemplate = templateService.getEmailTemplate(NEW_LISTINGS_CREATED);
        String body = String.format(emailTemplate.templateContent, newListingsMadeEvent.source(), newListingsMadeEvent.message(),
                newListingsMadeEvent.newListingsCount(), newListingsMadeEvent.timeStamp(), newListingsMadeEvent.source());
        sendMailToAdmins(newListingsMadeEvent.message(), body);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sendMailToAdmins(String subject, String body) {
        Log.info("Sending email to admins");
        List<String> emails = entityManager
                .createNativeQuery("SELECT email FROM USERS WHERE authorities LIKE '%ROLE_ADMIN%'")
                .getResultList()
                .stream()
                .map(result -> (String) result)
                .toList();

        submitEmailTasks(emails, subject, body, 0);
    }

    private void submitEmailTasks(List<String> recipients, String subject, String body, int attempts) {
        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            recipients.forEach(recipient -> {
                executor.submit(() -> {
                    try {
                        sendEmail(recipient, subject, body);
                    }
                    catch (Exception e) {
                        if (attempts >= MAX_RETRIES) {
                            Log.error(String.format("Failed to send email to %s. Error message: %s. Trying again", recipient, e.getMessage()));
                            submitEmailTasks(recipients, subject, body, attempts + 1);
                        }
                        else {
                            Log.error("Failed to send email to %s. Error message: " + e.getMessage());
                        }
                    }
                });
            }, virtualThreadExecutor);
        }
    }
}
