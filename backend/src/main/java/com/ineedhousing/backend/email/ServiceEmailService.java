package com.ineedhousing.backend.email;


import com.ineedhousing.backend.email.models.SuccessfulKeyRotationEvent;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Lazy
public class ServiceEmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Value("${support.email}")
    private String emailUsername;

    public ServiceEmailService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
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

    public void sendKeyRotationEmail(SuccessfulKeyRotationEvent event) {
        String emailMessage = "New key has been set after successful rotation.\nNew key: " + event.newKey();
        List<User> admins = userRepository.findUsersWithAdminRole();

        admins.forEach(admin -> {
            String adminEmail = admin.getEmail();
            try {
                sendEmail(adminEmail, event.message(), emailMessage);
            } catch (MessagingException e) {
                log.error("Failed to send email to {}. Reason: {}", adminEmail, e.getMessage());
            }
        });
    }
}
