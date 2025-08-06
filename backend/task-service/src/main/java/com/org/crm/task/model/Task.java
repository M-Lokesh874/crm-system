package com.org.crm.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Task entity for the CRM Task system
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    @Builder.Default
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Builder.Default
    private TaskType type = TaskType.GENERAL;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "related_type")
    private String relatedType; // e.g., LEAD, OPPORTUNITY, CUSTOMER

    @Column(name = "related_id")
    private Long relatedId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Task status enumeration
     */
    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED, OVERDUE, CANCELLED
    }

    /**
     * Task priority enumeration
     */
    public enum TaskPriority {
        LOW, MEDIUM, HIGH, URGENT
    }

    /**
     * Task type enumeration
     */
    public enum TaskType {
        GENERAL, FOLLOW_UP, MEETING, CALL, EMAIL, PROPOSAL, CONTRACT, SUPPORT, MAINTENANCE
    }

    /**
     * Check if task is active
     */
    public boolean isActive() {
        return TaskStatus.PENDING.equals(status) ||
               TaskStatus.IN_PROGRESS.equals(status);
    }

    /**
     * Check if task is completed
     */
    public boolean isCompleted() {
        return TaskStatus.COMPLETED.equals(status);
    }

    /**
     * Check if task is overdue
     */
    public boolean isOverdue() {
        return dueDate != null && dueDate.isBefore(LocalDateTime.now()) && !isCompleted();
    }
}