package com.ineedhousing.backend.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ineedhousing.backend.email.v1.EmailConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
class EmailConfigurationTest {

    private final String EMAIL_ADDRESS = "<EMAIL>";
    private final String EMAIL_PASSWORD = "<PASSWORD>";
    private EmailConfiguration emailConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailConfiguration = new EmailConfiguration();

    }
    @Test
    void testJavaMailSender() {
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        when(javaMailSender.getUsername()).thenReturn(EMAIL_ADDRESS);
        when(javaMailSender.getPassword()).thenReturn(EMAIL_PASSWORD);
        JavaMailSender mailSender = emailConfiguration.javaMailSender();
        assertNotNull(mailSender);
        assertEquals(EMAIL_ADDRESS, javaMailSender.getUsername());
        assertEquals(EMAIL_PASSWORD, javaMailSender.getPassword());
    }
}