package com.org.crm.notification.consumer;

import com.org.crm.common.events.UserRegisteredEvent;
import com.org.crm.notification.model.Notification;
import com.org.crm.notification.service.EmailService;
import com.org.crm.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final EmailService emailService;
    private final NotificationService notificationService;

    @RabbitListener(queues = "user.events.user.registered")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("📧 Received user registration event for: {}", event.getEmail());
        
        try {
            // Send welcome email
            emailService.sendWelcomeEmail(
                event.getEmail(),
                event.getUsername(),
                event.getFullName()
            );
            log.info("✅ Welcome email sent successfully to: {}", event.getEmail());
            
            // Save notification to database
            notificationService.createNotification(
                new NotificationService.CreateNotificationRequest(
                    Notification.NotificationType.INFO,
                    "Welcome email sent to " + event.getEmail(),
                    event.getEmail(),
                    "USER",
                    null
                )
            );
            log.info("✅ Notification saved to database for: {}", event.getEmail());
            
        } catch (Exception e) {
            log.error("❌ Failed to send welcome email to: {}", event.getEmail(), e);
        }
    }
} 