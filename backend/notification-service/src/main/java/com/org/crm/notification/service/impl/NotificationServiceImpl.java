package com.org.crm.notification.service.impl;

import com.org.crm.notification.model.Notification;
import com.org.crm.notification.repository.NotificationRepository;
import com.org.crm.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of NotificationService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        log.info("Creating new notification for recipient: {}", request.recipient());
        Notification notification = Notification.builder()
                .type(request.type())
                .message(request.message())
                .recipient(request.recipient())
                .status(Notification.NotificationStatus.UNREAD)
                .relatedType(request.relatedType())
                .relatedId(request.relatedId())
                .build();
        Notification saved = notificationRepository.save(notification);
        return NotificationResponse.fromNotification(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationResponse> getNotificationById(Long id) {
        return notificationRepository.findById(id).map(NotificationResponse::fromNotification);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
        notification.setStatus(Notification.NotificationStatus.READ);
        Notification updated = notificationRepository.save(notification);
        return NotificationResponse.fromNotification(updated);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByRecipient(String recipient, Pageable pageable) {
        return notificationRepository.findByRecipient(recipient, pageable).map(NotificationResponse::fromNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByStatus(Notification.NotificationStatus status, Pageable pageable) {
        return notificationRepository.findByStatus(status, pageable).map(NotificationResponse::fromNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByType(Notification.NotificationType type, Pageable pageable) {
        return notificationRepository.findByType(type, pageable).map(NotificationResponse::fromNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByRelatedEntity(String relatedType, Long relatedId) {
        return notificationRepository.findByRelatedTypeAndRelatedId(relatedType, relatedId)
                .stream().map(NotificationResponse::fromNotification).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByCreatedAtRange(LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findByCreatedAtBetween(startDate, endDate)
                .stream().map(NotificationResponse::fromNotification).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationStatistics getNotificationStatistics() {
        long total = notificationRepository.count();
        long unread = notificationRepository.countByStatus(Notification.NotificationStatus.UNREAD);
        long read = notificationRepository.countByStatus(Notification.NotificationStatus.READ);
        long archived = notificationRepository.countByStatus(Notification.NotificationStatus.ARCHIVED);
        long info = notificationRepository.countByType(Notification.NotificationType.INFO);
        long warning = notificationRepository.countByType(Notification.NotificationType.WARNING);
        long alert = notificationRepository.countByType(Notification.NotificationType.ALERT);
        long task = notificationRepository.countByType(Notification.NotificationType.TASK);
        long lead = notificationRepository.countByType(Notification.NotificationType.LEAD);
        long opportunity = notificationRepository.countByType(Notification.NotificationType.OPPORTUNITY);
        long customer = notificationRepository.countByType(Notification.NotificationType.CUSTOMER);
        return new NotificationStatistics(
                total, unread, read, archived, info, warning, alert, task, lead, opportunity, customer
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long getNotificationCountByRecipient(String recipient) {
        return notificationRepository.countByRecipient(recipient);
    }

    @Override
    @Transactional(readOnly = true)
    public long getNotificationCountByStatus(Notification.NotificationStatus status) {
        return notificationRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getNotificationCountByType(Notification.NotificationType type) {
        return notificationRepository.countByType(type);
    }
}