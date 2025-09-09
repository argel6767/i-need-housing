package com.ineedhousing.services;

import com.ineedhousing.models.EmailTemplate;
import com.ineedhousing.models.requests.VerificationCodeDto;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.CompletableFuture;

import static com.ineedhousing.constants.TemplateNames.EMAIL_VERIFICATION;
import static com.ineedhousing.constants.TemplateNames.RESET_PASSWORD;

@ApplicationScoped
public class ClientEmailService {

    @Inject
    Mailer mailer;

    @Inject
    TemplateService templateService;

    private final String VERIFICATION_EMAIL_SUBJECT = "Verify your email - INeedHousing";
    private final String FORGET_PASSWORD_SUBJECT = "Reset your password - INeedHousing";
    private final int MAX_TRIES = 10;

    public void sendEmail(String to, String subject, String body) {
        Mail mail = Mail.withHtml(to, subject, body);
        mailer.send(mail);
    }

    public void sendEmailVerificationEmail(VerificationCodeDto verificationCodeDto, int attempts) {
        EmailTemplate emailTemplate = templateService.getEmailTemplate(EMAIL_VERIFICATION);
        String body = String.format(emailTemplate.templateContent, verificationCodeDto.verificationCode());
        CompletableFuture.runAsync(() -> {
            try {
                sendEmail(verificationCodeDto.email(), VERIFICATION_EMAIL_SUBJECT, body);
                Log.info("Email verification email sent");
            }
            catch (Exception e) {
                if (attempts < MAX_TRIES) {
                    Log.error("Failed to send email verification email, trying once more", e);
                    sendEmailVerificationEmail(verificationCodeDto,  attempts + 1);
                }
                else {
                    Log.error("Failed to send verification email after max tries");
                }
            }
        });
    }

    public void sendResetPasswordEmail(VerificationCodeDto verificationCodeDto, int attempts) {
        EmailTemplate emailTemplate = templateService.getEmailTemplate(RESET_PASSWORD);
        String body = String.format(emailTemplate.templateContent, verificationCodeDto.verificationCode());
        CompletableFuture.runAsync(() -> {
            try {
                sendEmail(verificationCodeDto.email(), FORGET_PASSWORD_SUBJECT, body);
            }
            catch (Exception e) {
                if (attempts < MAX_TRIES) {
                    Log.error("Failed to send reset password email, trying once more", e);
                    sendResetPasswordEmail(verificationCodeDto, attempts + 1);
                }
                else {
                    Log.error("Failed to send password reset email after max tries");
                }
            }
        });
    }

}
