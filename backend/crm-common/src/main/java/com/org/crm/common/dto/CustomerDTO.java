package com.org.crm.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTOs for customer operations
 */
public class CustomerDTO {
    
    @Data
    public static class CreateCustomerRequest {
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be less than 50 characters")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be less than 50 characters")
        private String lastName;
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must be less than 100 characters")
        private String email;
        
        @Size(max = 20, message = "Phone number must be less than 20 characters")
        private String phone;
        
        @Size(max = 100, message = "Company name must be less than 100 characters")
        private String company;
        
        @Size(max = 50, message = "Industry must be less than 50 characters")
        private String industry;
        
        private Long assignedTo;
    }
    
    @Data
    public static class UpdateCustomerRequest {
        @Size(max = 50, message = "First name must be less than 50 characters")
        private String firstName;
        
        @Size(max = 50, message = "Last name must be less than 50 characters")
        private String lastName;
        
        @Size(max = 20, message = "Phone number must be less than 20 characters")
        private String phone;
        
        @Size(max = 100, message = "Company name must be less than 100 characters")
        private String company;
        
        @Size(max = 50, message = "Industry must be less than 50 characters")
        private String industry;
        
        private Long assignedTo;
    }
    
    @Data
    public static class CustomerResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String company;
        private String industry;
        private String status;
        private Long assignedTo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    public static class CustomerSearchRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String company;
        private String industry;
        private String status;
        private Long assignedTo;
        private Integer page = 0;
        private Integer size = 10;
        private String sortBy = "id";
        private String direction = "asc";
    }
} 