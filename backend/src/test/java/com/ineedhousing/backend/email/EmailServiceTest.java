package com.ineedhousing.backend.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private EmailService emailService;
    private final String TO = "recipient@example.com";
    private final String SUBJECT = "subject";
    private final String BODY = "body";

    @Mock
    private JavaMailSender mailSender = mock(JavaMailSender.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService();
        ReflectionTestUtils.setField(emailService, "mailSender", mailSender);
    }

    @Test
    void testSendEmail() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper helper = mock(MimeMessageHelper.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendEmail(TO, SUBJECT, BODY);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }
}