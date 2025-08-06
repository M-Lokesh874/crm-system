package com.org.crm.notification.repository;

import com.org.crm.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient(String recipient);

    List<Notification> findByStatus(Notification.NotificationStatus status);

    List<Notification> findByType(Notification.NotificationType type);

    List<Notification> findByRelatedTypeAndRelatedId(String relatedType, Long relatedId);

    Page<Notification> findByRecipient(String recipient, Pageable pageable);

    Page<Notification> findByStatus(Notification.NotificationStatus status, Pageable pageable);

    Page<Notification> findByType(Notification.NotificationType type, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.createdAt BETWEEN :startDate AND :endDate")
    List<Notification> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);

    long countByRecipient(String recipient);

    long countByStatus(Notification.NotificationStatus status);

    long countByType(Notification.NotificationType type);
}