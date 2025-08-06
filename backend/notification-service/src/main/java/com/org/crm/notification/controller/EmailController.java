package com.org.crm.notification.controller;

import com.org.crm.notification.dto.EmailRequest;
import com.org.crm.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/test")
    public ResponseEntity<String> testEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            log.error("Failed to send test email", e);
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String fullName) {
        try {
            emailService.sendWelcomeEmail(email, username, fullName);
            return ResponseEntity.ok("Welcome email sent successfully!");
        } catch (Exception e) {
            log.error("Failed to send welcome email", e);
            return ResponseEntity.internalServerError().body("Failed to send welcome email: " + e.getMessage());
        }
    }
} 