package com.org.crm.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Customer-related events for the CRM system
 */
public class CustomerEvents {
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CustomerCreatedEvent extends BaseEvent {
        private Long customerId;
        private String email;
        private String firstName;
        private String lastName;
        private String company;
        private String industry;
        private String assignedTo;
        
        public CustomerCreatedEvent() {
            super("customer.created", "customer-service");
        }
        
        public CustomerCreatedEvent(Long customerId, String email, String firstName, String lastName, 
                                 String company, String industry, String assignedTo) {
            super("customer.created", "customer-service");
            this.customerId = customerId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;
            this.industry = industry;
            this.assignedTo = assignedTo;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CustomerUpdatedEvent extends BaseEvent {
        private Long customerId;
        private String email;
        private String firstName;
        private String lastName;
        private String company;
        private String industry;
        private String assignedTo;
        
        public CustomerUpdatedEvent() {
            super("customer.updated", "customer-service");
        }
        
        public CustomerUpdatedEvent(Long customerId, String email, String firstName, String lastName, 
                                 String company, String industry, String assignedTo) {
            super("customer.updated", "customer-service");
            this.customerId = customerId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;
            this.industry = industry;
            this.assignedTo = assignedTo;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CustomerDeletedEvent extends BaseEvent {
        private Long customerId;
        private String email;
        
        public CustomerDeletedEvent() {
            super("customer.deleted", "customer-service");
        }
        
        public CustomerDeletedEvent(Long customerId, String email) {
            super("customer.deleted", "customer-service");
            this.customerId = customerId;
            this.email = email;
        }
    }
} 