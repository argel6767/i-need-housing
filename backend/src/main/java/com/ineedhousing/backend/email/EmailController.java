package com.ineedhousing.backend.email;

import com.ineedhousing.backend.email.models.SuccessfulKeyRotationEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final ServiceEmailService serviceEmailService;

    public EmailController( ServiceEmailService serviceEmailService) {
        this.serviceEmailService = serviceEmailService;
    }

    @PostMapping("/notifications/key-rotation")
    public ResponseEntity<String> sendNotificationKeyRotation(@RequestBody SuccessfulKeyRotationEvent event)  {
        serviceEmailService.sendKeyRotationEmail(event);
        return ResponseEntity.ok().body("Successfully sent key rotation");
    }
}
