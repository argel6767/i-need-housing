package com.ineedhousing.backend.email;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ineedhousing.backend.email.models.SuccessfulKeyRotationEvent;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServiceEmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${support.email}")
    private String emailUsername;

    public ServiceEmailService(JavaMailSender mailSender, UserRepository userRepository, ObjectMapper objectMapper) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(emailUsername);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

    @Async
    public void sendKeyRotationEmail(String event) {
        log.info("Sending key rotation email with data: {}", event);
        SuccessfulKeyRotationEvent eventParsed = objectMapper.convertValue(event, SuccessfulKeyRotationEvent.class);
        log.info("Processing the following event: {}", event);
        if (eventParsed.newKey() == null || eventParsed.newKey().isEmpty()) {
            throw new IllegalArgumentException("New Key is null or empty");
        }
        log.info("Sending key rotation email with subject {} and new key", eventParsed.message());
        if (eventParsed.message() == null || eventParsed.message().isEmpty()) {
            SuccessfulKeyRotationEvent nonNull = new SuccessfulKeyRotationEvent("Successful Key Rotation", eventParsed.newKey(), eventParsed.timeStamp());
        }
        String body = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Key Rotation Notification</title>
              <style>
                @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
                :root {
                  --primary: #176087;
                  --foreground: #000000;
                  --slate-50: #f8fafc;
                  --slate-100: #f1f5f9;
                  --slate-200: #e2e8f0;
                }
                body {
                  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                  margin: 0;
                  padding: 0;
                  line-height: 1.6;
                  background-color: #f5f5f5;
                }
                .container {
                  max-width: 600px;
                  margin: 0 auto;
                  background-color: #ffffff;
                  border-radius: 8px;
                  overflow: hidden;
                  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
                }
                .header {
                  padding: 24px;
                  background-color: #176087;
                  text-align: center;
                }
                .header h1 {
                  margin: 0;
                  color: white;
                  font-size: 24px;
                  font-weight: 700;
                  letter-spacing: 0.5px;
                }
                .content {
                  padding: 32px 24px;
                  background-color: #ffffff;
                }
                .title {
                  font-size: 20px;
                  font-weight: 600;
                  color: #000000;
                  margin-top: 0;
                  margin-bottom: 16px;
                }
                .text {
                  font-size: 16px;
                  color: #4b5563;
                  margin-bottom: 24px;
                }
                .code-container {
                  background-color: #f1f5f9;
                  border-radius: 6px;
                  padding: 16px;
                  text-align: center;
                  margin-bottom: 24px;
                  border: 1px solid #e2e8f0;
                }
                .key-value {
                  font-family: 'Courier New', monospace;
                  font-size: 18px;
                  font-weight: 700;
                  letter-spacing: 2px;
                  color: #176087;
                  word-break: break-all;
                }
                .footer {
                  padding: 24px;
                  background-color: #f8fafc;
                  text-align: center;
                  border-top: 1px solid #e2e8f0;
                }
                .footer-text {
                  font-size: 14px;
                  color: #6b7280;
                }
                .help-text {
                  font-size: 13px;
                  color: #9ca3af;
                  text-align: center;
                  margin-top: 8px;
                }
              </style>
            </head>
            <body>
              <div style="padding: 20px;">
                <div class="container">
                  <div class="header">
                    <h1>INeedHousing</h1>
                  </div>
                  <div class="content">
                    <h1 class="title">Key Rotation Successful</h1>
                    <p class="text">A new key has been set after a successful rotation. Please find the new key below:</p>
                    <div class="code-container">
                      <div class="key-value">%s</div>
                    </div>
                    <p class="text">If you did not request this change, please contact support immediately.</p>
                    <p class="help-text">Having trouble? Contact our support team.</p>
                  </div>
                  <div class="footer">
                    <p class="footer-text">© 2025 INeedHousing. All rights reserved.</p>
                  </div>
                </div>
              </div>
            </body>
            </html>
            """, eventParsed.newKey());
        List<User> admins = userRepository.findUsersWithAdminRole();
        admins.forEach(admin -> {
            try {
                sendEmail(admin.getEmail(), eventParsed.message(), body);
            } catch (MessagingException e) {
                log.error("Failed to send email to {}", admin.getEmail(), e);
            }
        });
    }
}
