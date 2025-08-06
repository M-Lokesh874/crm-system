package com.org.crm.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTOs for task operations
 */
public class TaskDTO {
    
    @Data
    public static class CreateTaskRequest {
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be less than 200 characters")
        private String title;
        
        @Size(max = 1000, message = "Description must be less than 1000 characters")
        private String description;
        
        @NotBlank(message = "Type is required")
        private String type;
        
        @NotBlank(message = "Priority is required")
        private String priority;
        
        @NotNull(message = "Assigned to is required")
        private Long assignedTo;
        
        private Long customerId;
        
        private Long leadId;
        
        @NotNull(message = "Due date is required")
        private LocalDateTime dueDate;
    }
    
    @Data
    public static class UpdateTaskRequest {
        @Size(max = 200, message = "Title must be less than 200 characters")
        private String title;
        
        @Size(max = 1000, message = "Description must be less than 1000 characters")
        private String description;
        
        private String type;
        
        private String priority;
        
        private Long assignedTo;
        
        private Long customerId;
        
        private Long leadId;
        
        private LocalDateTime dueDate;
    }
    
    @Data
    public static class TaskResponse {
        private Long id;
        private String title;
        private String description;
        private String type;
        private String priority;
        private Long assignedTo;
        private Long customerId;
        private Long leadId;
        private LocalDateTime dueDate;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    public static class TaskSearchRequest {
        private String title;
        private String type;
        private String priority;
        private String status;
        private Long assignedTo;
        private Long customerId;
        private Long leadId;
        private LocalDateTime dueDateFrom;
        private LocalDateTime dueDateTo;
        private Integer page = 0;
        private Integer size = 10;
        private String sortBy = "id";
        private String direction = "asc";
    }
    
    @Data
    public static class UpdateTaskStatusRequest {
        @NotBlank(message = "Status is required")
        private String status;
    }
} 