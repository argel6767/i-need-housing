package com.ineedhousing.services;

import com.ineedhousing.models.EmailTemplate;
import com.ineedhousing.models.requests.VerificationCodeDto;
import com.ineedhousing.qualifiers.VirtualThreadPool;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.ineedhousing.constants.TemplateNames.EMAIL_VERIFICATION;
import static com.ineedhousing.constants.TemplateNames.RESET_PASSWORD;

@ApplicationScoped
public class ClientEmailService {

    @Inject
    Mailer mailer;

    @Inject
    TemplateService templateService;

    @Inject
    @VirtualThreadPool
    ExecutorService virtualThreadExecutor;

    @Inject
    LogService log;

    private final String VERIFICATION_EMAIL_SUBJECT = "Verify your email - INeedHousing";
    private final String FORGET_PASSWORD_SUBJECT = "Reset your password - INeedHousing";
    private final int MAX_TRIES = 10;

    public void sendEmail(String to, String subject, String body) {
        String privateEmail = to.substring(0,3) + "*****";
        log.info("Sending mail to: " + privateEmail);
        Mail mail = Mail.withHtml(to, subject, body);
        mailer.send(mail);
    }

    public void sendEmailVerificationEmail(VerificationCodeDto verificationCodeDto, int attempts) {
        verifyPayload(verificationCodeDto);
        EmailTemplate emailTemplate = templateService.getEmailTemplate(EMAIL_VERIFICATION);
        String body = String.format(emailTemplate.templateContent, verificationCodeDto.verificationCode());
        CompletableFuture.runAsync(() -> {
            try {
                sendEmail(verificationCodeDto.email(), VERIFICATION_EMAIL_SUBJECT, body);
                log.info("Email verification email sent");
            }
            catch (Exception e) {
                String privateEmail = verificationCodeDto.email().substring(0,3) + "*****";
                if (attempts < MAX_TRIES) {
                    log.error(String.format("Failed to send email verification to new user %s, trying once more. Error Message: %s", privateEmail, e.getMessage()));
                    sendEmailVerificationEmail(verificationCodeDto,  attempts + 1);
                }
                else {
                    log.error(String.format("Failed to send verification email to new user %s after max tries", privateEmail));
                }
            }
        }, virtualThreadExecutor);
    }

    public void sendResetPasswordEmail(VerificationCodeDto verificationCodeDto, int attempts) {
        verifyPayload(verificationCodeDto);
        EmailTemplate emailTemplate = templateService.getEmailTemplate(RESET_PASSWORD);
        String body = String.format(emailTemplate.templateContent, verificationCodeDto.verificationCode());
        CompletableFuture.runAsync(() -> {
            try {
                sendEmail(verificationCodeDto.email(), FORGET_PASSWORD_SUBJECT, body);
            }
            catch (Exception e) {
                String privateEmail = verificationCodeDto.email().substring(0,3) + "*****";
                if (attempts < MAX_TRIES) {
                    log.error(String.format("Failed to send reset password email to user %s, trying once more. Error Message: %s", privateEmail, e.getMessage()));
                    sendResetPasswordEmail(verificationCodeDto, attempts + 1);
                }
                else {
                    Log.error("Failed to send password reset email after max tries");
                }
            }
        }, virtualThreadExecutor);
    }

    private void verifyPayload(VerificationCodeDto verificationCodeDto) {
        if (verificationCodeDto == null || verificationCodeDto.verificationCode() == null ||  verificationCodeDto.email() == null) {
            throw new BadRequestException("Invalid verification payload");
        }
    }

}
