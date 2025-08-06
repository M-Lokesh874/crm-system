package com.org.crm.notification.service;

import com.org.crm.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Notification operations
 */
public interface NotificationService {

    NotificationResponse createNotification(CreateNotificationRequest request);

    Optional<NotificationResponse> getNotificationById(Long id);

    NotificationResponse markAsRead(Long id);

    void deleteNotification(Long id);

    Page<NotificationResponse> getNotificationsByRecipient(String recipient, Pageable pageable);

    Page<NotificationResponse> getNotificationsByStatus(Notification.NotificationStatus status, Pageable pageable);

    Page<NotificationResponse> getNotificationsByType(Notification.NotificationType type, Pageable pageable);

    List<NotificationResponse> getNotificationsByRelatedEntity(String relatedType, Long relatedId);

    List<NotificationResponse> getNotificationsByCreatedAtRange(LocalDateTime startDate, LocalDateTime endDate);

    NotificationStatistics getNotificationStatistics();

    long getNotificationCountByRecipient(String recipient);

    long getNotificationCountByStatus(Notification.NotificationStatus status);

    long getNotificationCountByType(Notification.NotificationType type);

    /**
     * Notification response DTO
     */
    record NotificationResponse(
            Long id,
            Notification.NotificationType type,
            String message,
            String recipient,
            Notification.NotificationStatus status,
            String relatedType,
            Long relatedId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static NotificationResponse fromNotification(Notification n) {
            return new NotificationResponse(
                    n.getId(),
                    n.getType(),
                    n.getMessage(),
                    n.getRecipient(),
                    n.getStatus(),
                    n.getRelatedType(),
                    n.getRelatedId(),
                    n.getCreatedAt(),
                    n.getUpdatedAt()
            );
        }
    }

    /**
     * Create notification request DTO
     */
    record CreateNotificationRequest(
            Notification.NotificationType type,
            String message,
            String recipient,
            String relatedType,
            Long relatedId
    ) {}

    /**
     * Notification statistics DTO
     */
    record NotificationStatistics(
            long totalNotifications,
            long unreadNotifications,
            long readNotifications,
            long archivedNotifications,
            long infoNotifications,
            long warningNotifications,
            long alertNotifications,
            long taskNotifications,
            long leadNotifications,
            long opportunityNotifications,
            long customerNotifications
    ) {}
}