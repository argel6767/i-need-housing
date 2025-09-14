package com.ineedhousing.backend.email.v2;

import com.ineedhousing.backend.email.models.VerifyUserDto;
import com.ineedhousing.backend.email.v1.ClientEmailService;
import com.ineedhousing.backend.ping_services.models.models.PingAllServicesEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingEmailServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingKeymasterServiceEvent;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class EmailService {

    private final ClientEmailService clientEmailService;
    private final RestClient restClient;

    public EmailService(ClientEmailService clientEmailService, @Qualifier("email_service") RestClient restClient) {
        this.clientEmailService = clientEmailService;
        this.restClient = restClient;
    }

    public void sendVerificationCodeEmail(VerifyUserDto verifyUserDto) throws MessagingException {
        String privatizedEmail = verifyUserDto.getEmail().substring(0, 2)+"*****";
        log.info("Sending verification code email for user {} to Email Service", privatizedEmail);
        try {
            String response = restClient.post()
                    .uri("/v1/emails/clients/verify")
                    .body(verifyUserDto)
                    .retrieve()
                    .toEntity(String.class)
                    .getBody();
            log.info(response);
        }
        catch (Exception e) {
            log.error("Failed to send verification code email request to Email Service. Error message: {}. Falling back to legacy email service", e.getMessage());
            clientEmailService.sendVerificationEmail(verifyUserDto.getVerificationCode(), verifyUserDto.getEmail());
        }
    }

    public void sendResetPasswordEmail(VerifyUserDto verifyUserDto) throws MessagingException {
        String privatizedEmail = verifyUserDto.getEmail().substring(0, 2)+"*****";
        log.info("Sending reset password email for user {} to Email Service", privatizedEmail);
        try {
            String response = restClient.post()
                    .uri("/v1/emails/clients/reset-password")
                    .body(verifyUserDto)
                    .retrieve()
                    .toEntity(String.class)
                    .getBody();
            log.info(response);
        }
        catch (Exception e) {
            log.error("Failed to send reset password email request to Email Service. Error message: {}. Falling back to legacy email service ", e.getMessage());
            clientEmailService.sendVerificationEmail(verifyUserDto.getVerificationCode(), verifyUserDto.getEmail());
        }
    }

    public void pingService() {
        try {
            String response = restClient.post()
                    .uri("/ping")
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);
            log.info(response);
        }
        catch (RestClientException e) {
            log.warn("Service was pinged but overall request was not successful: {},", e.getMessage());
        }
        catch (Exception e) {
            log.error("Service was unsuccessful: {},", e.getMessage());
        }
    }

    @EventListener
    @Async
    public void onPingAllServicesEvent(PingAllServicesEvent pingAllServicesEvent) {
        log.info("Pinging Email Service during all services pinged event");
        pingService();
    }

    @EventListener
    @Async
    public void onPingServiceEvent(PingEmailServiceEvent pingServiceEvent) {
        log.info("Pinging Email Service");
        pingService();
    }
}
