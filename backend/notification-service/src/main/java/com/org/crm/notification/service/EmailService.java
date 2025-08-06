package com.org.crm.notification.service;

import com.org.crm.notification.dto.EmailRequest;

public interface EmailService {
    void sendWelcomeEmail(String to, String username, String fullName);
    void sendEmail(EmailRequest emailRequest);
    void sendNotificationEmail(String to, String subject, String content);
} 