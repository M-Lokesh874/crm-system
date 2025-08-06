package com.org.crm.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Task-related events for the CRM system
 */
public class TaskEvents {
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TaskCreatedEvent extends BaseEvent {
        private Long taskId;
        private String title;
        private String description;
        private String type;
        private String priority;
        private String assignedTo;
        private Long customerId;
        private Long leadId;
        private LocalDateTime dueDate;
        
        public TaskCreatedEvent() {
            super("task.created", "task-service");
        }
        
        public TaskCreatedEvent(Long taskId, String title, String description, String type, 
                              String priority, String assignedTo, Long customerId, Long leadId, LocalDateTime dueDate) {
            super("task.created", "task-service");
            this.taskId = taskId;
            this.title = title;
            this.description = description;
            this.type = type;
            this.priority = priority;
            this.assignedTo = assignedTo;
            this.customerId = customerId;
            this.leadId = leadId;
            this.dueDate = dueDate;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TaskAssignedEvent extends BaseEvent {
        private Long taskId;
        private String oldAssignedTo;
        private String newAssignedTo;
        private String title;
        private LocalDateTime dueDate;
        
        public TaskAssignedEvent() {
            super("task.assigned", "task-service");
        }
        
        public TaskAssignedEvent(Long taskId, String oldAssignedTo, String newAssignedTo, String title, LocalDateTime dueDate) {
            super("task.assigned", "task-service");
            this.taskId = taskId;
            this.oldAssignedTo = oldAssignedTo;
            this.newAssignedTo = newAssignedTo;
            this.title = title;
            this.dueDate = dueDate;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TaskCompletedEvent extends BaseEvent {
        private Long taskId;
        private String assignedTo;
        private String title;
        private LocalDateTime completedAt;
        
        public TaskCompletedEvent() {
            super("task.completed", "task-service");
        }
        
        public TaskCompletedEvent(Long taskId, String assignedTo, String title, LocalDateTime completedAt) {
            super("task.completed", "task-service");
            this.taskId = taskId;
            this.assignedTo = assignedTo;
            this.title = title;
            this.completedAt = completedAt;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TaskDueSoonEvent extends BaseEvent {
        private Long taskId;
        private String assignedTo;
        private String title;
        private LocalDateTime dueDate;
        
        public TaskDueSoonEvent() {
            super("task.due.soon", "task-service");
        }
        
        public TaskDueSoonEvent(Long taskId, String assignedTo, String title, LocalDateTime dueDate) {
            super("task.due.soon", "task-service");
            this.taskId = taskId;
            this.assignedTo = assignedTo;
            this.title = title;
            this.dueDate = dueDate;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TaskUpdatedEvent extends BaseEvent {
        private Long taskId;
        private String title;
        private String description;
        private String type;
        private String priority;
        private String assignedTo;
        private Long customerId;
        private Long leadId;
        private LocalDateTime dueDate;
        
        public TaskUpdatedEvent() {
            super("task.updated", "task-service");
        }
        
        public TaskUpdatedEvent(Long taskId, String title, String description, String type, 
                              String priority, String assignedTo, Long customerId, Long leadId, LocalDateTime dueDate) {
            super("task.updated", "task-service");
            this.taskId = taskId;
            this.title = title;
            this.description = description;
            this.type = type;
            this.priority = priority;
            this.assignedTo = assignedTo;
            this.customerId = customerId;
            this.leadId = leadId;
            this.dueDate = dueDate;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TaskDeletedEvent extends BaseEvent {
        private Long taskId;
        private String title;
        private String assignedTo;
        
        public TaskDeletedEvent() {
            super("task.deleted", "task-service");
        }
        
        public TaskDeletedEvent(Long taskId, String title, String assignedTo) {
            super("task.deleted", "task-service");
            this.taskId = taskId;
            this.title = title;
            this.assignedTo = assignedTo;
        }
    }
} 