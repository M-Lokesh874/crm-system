package com.org.crm.notification.service.impl;

import com.org.crm.notification.dto.EmailRequest;
import com.org.crm.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${crm.notification.email.from-name:CRM System}")
    private String fromName;

    @Override
    public void sendWelcomeEmail(String to, String username, String fullName) {
        log.info("Sending welcome email to: {}", to);
        
        String subject = "Welcome to CRM System!";
        String content = buildWelcomeEmailContent(username, fullName);
        
        sendEmail(EmailRequest.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .build());
    }

    @Override
    public void sendEmail(EmailRequest emailRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getContent());
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", emailRequest.getTo());
            
        } catch (Exception e) {
            log.error("Failed to send email to: {}", emailRequest.getTo(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendNotificationEmail(String to, String subject, String content) {
        sendEmail(EmailRequest.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .build());
    }

    private String buildWelcomeEmailContent(String username, String fullName) {
        return String.format("""
            Dear %s,
            
            Welcome to the CRM System! Your account has been successfully created.
            
            Account Details:
            - Username: %s
            - Full Name: %s
            
            You can now log in to the CRM system and start managing your customers, leads, and opportunities.
            
            If you have any questions, please contact our support team.
            
            Best regards,
            CRM System Team
            """, fullName, username, fullName);
    }
} 