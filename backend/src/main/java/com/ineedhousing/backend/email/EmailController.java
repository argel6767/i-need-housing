package com.ineedhousing.backend.email;

import com.ineedhousing.backend.email.models.SuccessfulKeyRotationEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/emails")
public class EmailController {

    private final ServiceEmailService serviceEmailService;

    public EmailController( ServiceEmailService serviceEmailService) {
        this.serviceEmailService = serviceEmailService;
    }

    @PostMapping("/notifications/key-rotation")
    public ResponseEntity<String> sendNotificationKeyRotation(@RequestBody SuccessfulKeyRotationEvent event, HttpServletRequest request) throws IOException {
        String body = new BufferedReader(request.getReader())
                .lines()
                .collect(Collectors.joining("\n"));
        log.info("Body for request: {}", body);
        serviceEmailService.sendKeyRotationEmail(event);
        return ResponseEntity.ok().body("Successfully sent key rotation");
    }
}
