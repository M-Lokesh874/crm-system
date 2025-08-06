package com.org.crm.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTOs for lead operations
 */
public class LeadDTO {
    
    @Data
    public static class CreateLeadRequest {
        @NotNull(message = "Customer ID is required")
        private Long customerId;
        
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be less than 200 characters")
        private String title;
        
        @Size(max = 1000, message = "Description must be less than 1000 characters")
        private String description;
        
        private BigDecimal value;
        
        @NotBlank(message = "Stage is required")
        private String stage;
        
        private Long assignedTo;
        
        private LocalDate expectedCloseDate;
    }
    
    @Data
    public static class UpdateLeadRequest {
        @Size(max = 200, message = "Title must be less than 200 characters")
        private String title;
        
        @Size(max = 1000, message = "Description must be less than 1000 characters")
        private String description;
        
        private BigDecimal value;
        
        private String stage;
        
        private Long assignedTo;
        
        private LocalDate expectedCloseDate;
    }
    
    @Data
    public static class LeadResponse {
        private Long id;
        private Long customerId;
        private String title;
        private String description;
        private BigDecimal value;
        private String stage;
        private Long assignedTo;
        private LocalDate expectedCloseDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    public static class LeadSearchRequest {
        private Long customerId;
        private String title;
        private String stage;
        private Long assignedTo;
        private LocalDate expectedCloseDateFrom;
        private LocalDate expectedCloseDateTo;
        private Integer page = 0;
        private Integer size = 10;
        private String sortBy = "id";
        private String direction = "asc";
    }
    
    @Data
    public static class UpdateLeadStageRequest {
        @NotBlank(message = "Stage is required")
        private String stage;
    }
} 