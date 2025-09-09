package com.ineedhousing.backend.email.v1;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * houses the business logic of formatting an email
 */
@Log
@Service
public class ClientEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${support.email}")
    private String emailUsername;

    /*
     * sending verification email
     * takes in to whom its being sent to & subject and body
     */
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
    public void sendVerificationEmail(String code, String email) throws MessagingException {
        log.info("sending verification email for user " + email);
        String subject = "Verification Email";
        String body = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Verify Your Account</title>
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
                    .header img {
                      height: 40px;
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
                    .verification-code {
                      font-family: 'Courier New', monospace;
                      font-size: 24px;
                      font-weight: 700;
                      letter-spacing: 4px;
                      color: #176087;
                    }
                    .button {
                      display: inline-block;
                      background-color: #176087;
                      color: white;
                      text-decoration: none;
                      padding: 12px 24px;
                      border-radius: 6px;
                      font-weight: 500;
                      margin-bottom: 24px;
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
                        <h1 style="margin: 0; color: white; font-size: 24px; font-weight: 700; letter-spacing: 0.5px;">INeedHousing</h1>
                      </div>
                      <div class="content">
                        <h1 class="title">Verify Your Email Address</h1>
                        <p class="text">Thanks for signing up! Please enter the verification code below to complete your account setup.</p>
                        <div class="code-container">
                          <div class="verification-code">%s</div>
                        </div>
                        <p class="text">This code will expire in 10 minutes. If you didn't request this code, you can safely ignore this email.</p>
                        <p class="help-text">Having trouble? Contact our support team.</p>
                      </div>
                      <div class="footer">
                        <p class="footer-text">© 2025 INeedHousing. All rights reserved.</p>
                      </div>
                    </div>
                  </div>
                </body>
                </html>""", code);
        sendEmail(email, subject, body);
    }

    @Async
    public void sendResetPasswordEmail(String code, String email) throws MessagingException {
        log.info("sending forgot password email for user " + email);
        String subject = "Reset Password";
        String body = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Verify Your Account</title>
                  <style>
                    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
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
                    .header img {
                      height: 40px;
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
                    .verification-code {
                      font-family: 'Courier New', monospace;
                      font-size: 24px;
                      font-weight: 700;
                      letter-spacing: 4px;
                      color: #176087;
                    }
                    .button {
                      display: inline-block;
                      background-color: #176087;
                      color: white;
                      text-decoration: none;
                      padding: 12px 24px;
                      border-radius: 6px;
                      font-weight: 500;
                      margin-bottom: 24px;
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
                        <h1 style="margin: 0; color: white; font-size: 24px; font-weight: 700; letter-spacing: 0.5px;">INeedHousing</h1>
                      </div>
                      <div class="content">
                        <h1 class="title">Verify Your Email Address</h1>
                        <p class="text">Thanks for signing up! Please enter the verification code below to complete your account setup.</p>
                        <div class="code-container">
                          <div class="verification-code">%s</div>
                        </div>
                        <p class="text">This code will expire in 10 minutes. If you didn't request this code, you can safely ignore this email.</p>
                        <p class="help-text">Having trouble? Contact our support team.</p>
                      </div>
                      <div class="footer">
                        <p class="footer-text">© 2025 INeedHousing. All rights reserved.</p>
                      </div>
                    </div>
                  </div>
                </body>
                </html>""", code);
        sendEmail(email, subject, body);
    }
}
