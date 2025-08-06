package com.org.crm.notification.consumer;

import com.org.crm.common.events.BaseEvent;
import com.org.crm.notification.model.Notification;
import com.org.crm.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for CRM events to generate notifications
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = {"customer.events.queue", "lead.events.queue", "task.events.queue"})
    public void handleCrmEvent(BaseEvent event) {
        log.info("[NotificationEventConsumer] Received event: {} - ID: {} - Source: {} - Timestamp: {}", 
                event.getEventType(), event.getEventId(), event.getSource(), event.getTimestamp());
        
        try {
            // Create notification based on event type
            Notification.NotificationType notificationType = getNotificationType(event.getEventType());
            String message = buildNotificationMessage(event);
            
            notificationService.createNotification(
                new NotificationService.CreateNotificationRequest(
                    notificationType,
                    message,
                    "admin@crm.com", // Default recipient - can be enhanced later
                    event.getEventType(),
                    null
                )
            );
            
            log.info("✅ Notification saved for event: {} - {}", event.getEventType(), event.getEventId());
            
        } catch (Exception e) {
            log.error("❌ Failed to create notification for event: {}", event.getEventType(), e);
        }
    }
    
    private Notification.NotificationType getNotificationType(String eventType) {
        if (eventType.contains("CUSTOMER")) {
            return Notification.NotificationType.CUSTOMER;
        } else if (eventType.contains("LEAD")) {
            return Notification.NotificationType.LEAD;
        } else if (eventType.contains("TASK")) {
            return Notification.NotificationType.TASK;
        } else if (eventType.contains("OPPORTUNITY")) {
            return Notification.NotificationType.OPPORTUNITY;
        } else {
            return Notification.NotificationType.INFO;
        }
    }
    
    private String buildNotificationMessage(BaseEvent event) {
        return String.format("Event %s occurred at %s", 
                event.getEventType(), 
                event.getTimestamp());
    }
}