package com.org.crm.notification.controller;

import com.org.crm.notification.model.Notification;
import com.org.crm.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Notification operations
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notification Management", description = "APIs for managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create a new notification", description = "Creates a new notification")
    public ResponseEntity<NotificationService.NotificationResponse> createNotification(
            @Valid @RequestBody NotificationService.CreateNotificationRequest request) {
        log.info("Creating new notification for recipient: {}", request.recipient());
        NotificationService.NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieves a notification by its ID")
    public ResponseEntity<NotificationService.NotificationResponse> getNotificationById(
            @Parameter(description = "Notification ID") @PathVariable Long id) {
        Optional<NotificationService.NotificationResponse> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Marks a notification as read")
    public ResponseEntity<NotificationService.NotificationResponse> markAsRead(
            @Parameter(description = "Notification ID") @PathVariable Long id) {
        try {
            NotificationService.NotificationResponse response = notificationService.markAsRead(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error marking notification as read: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Deletes a notification by its ID")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Notification ID") @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recipient/{recipient}")
    @Operation(summary = "Get notifications by recipient", description = "Retrieves notifications for a recipient with pagination")
    public ResponseEntity<Page<NotificationService.NotificationResponse>> getNotificationsByRecipient(
            @Parameter(description = "Recipient") @PathVariable String recipient,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationService.NotificationResponse> notifications = notificationService.getNotificationsByRecipient(recipient, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get notifications by status", description = "Retrieves notifications by status with pagination")
    public ResponseEntity<Page<NotificationService.NotificationResponse>> getNotificationsByStatus(
            @Parameter(description = "Notification status") @PathVariable Notification.NotificationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationService.NotificationResponse> notifications = notificationService.getNotificationsByStatus(status, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get notifications by type", description = "Retrieves notifications by type with pagination")
    public ResponseEntity<Page<NotificationService.NotificationResponse>> getNotificationsByType(
            @Parameter(description = "Notification type") @PathVariable Notification.NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationService.NotificationResponse> notifications = notificationService.getNotificationsByType(type, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/related")
    @Operation(summary = "Get notifications by related entity", description = "Retrieves notifications by related entity type and ID")
    public ResponseEntity<List<NotificationService.NotificationResponse>> getNotificationsByRelatedEntity(
            @RequestParam String relatedType,
            @RequestParam Long relatedId) {
        List<NotificationService.NotificationResponse> notifications = notificationService.getNotificationsByRelatedEntity(relatedType, relatedId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/created-at-range")
    @Operation(summary = "Get notifications by created at range", description = "Retrieves notifications created in a given range")
    public ResponseEntity<List<NotificationService.NotificationResponse>> getNotificationsByCreatedAtRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<NotificationService.NotificationResponse> notifications = notificationService.getNotificationsByCreatedAtRange(start, end);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get notification statistics", description = "Retrieves comprehensive notification statistics")
    public ResponseEntity<NotificationService.NotificationStatistics> getNotificationStatistics() {
        NotificationService.NotificationStatistics statistics = notificationService.getNotificationStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count/recipient/{recipient}")
    @Operation(summary = "Get notification count by recipient", description = "Retrieves the count of notifications for a recipient")
    public ResponseEntity<Long> getNotificationCountByRecipient(
            @Parameter(description = "Recipient") @PathVariable String recipient) {
        long count = notificationService.getNotificationCountByRecipient(recipient);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get notification count by status", description = "Retrieves the count of notifications by status")
    public ResponseEntity<Long> getNotificationCountByStatus(
            @Parameter(description = "Notification status") @PathVariable Notification.NotificationStatus status) {
        long count = notificationService.getNotificationCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/type/{type}")
    @Operation(summary = "Get notification count by type", description = "Retrieves the count of notifications by type")
    public ResponseEntity<Long> getNotificationCountByType(
            @Parameter(description = "Notification type") @PathVariable Notification.NotificationType type) {
        long count = notificationService.getNotificationCountByType(type);
        return ResponseEntity.ok(count);
    }
}