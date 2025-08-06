package com.org.crm.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Lead-related events for the CRM system
 */
public class LeadEvents {
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadCreatedEvent extends BaseEvent {
        private Long leadId;
        private String email;
        private String firstName;
        private String lastName;
        private String company;
        private String industry;
        private String assignedTo;
        private BigDecimal expectedValue;
        private String status;
        
        public LeadCreatedEvent() {
            super("lead.created", "sales-service");
        }
        
        public LeadCreatedEvent(Long leadId, String email, String firstName, String lastName, 
                              String company, String industry, String assignedTo, BigDecimal expectedValue, String status) {
            super("lead.created", "sales-service");
            this.leadId = leadId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;
            this.industry = industry;
            this.assignedTo = assignedTo;
            this.expectedValue = expectedValue;
            this.status = status;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadUpdatedEvent extends BaseEvent {
        private Long leadId;
        private String email;
        private String firstName;
        private String lastName;
        private String company;
        private String industry;
        private String assignedTo;
        private BigDecimal expectedValue;
        private String status;
        
        public LeadUpdatedEvent() {
            super("lead.updated", "sales-service");
        }
        
        public LeadUpdatedEvent(Long leadId, String email, String firstName, String lastName, 
                              String company, String industry, String assignedTo, BigDecimal expectedValue, String status) {
            super("lead.updated", "sales-service");
            this.leadId = leadId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.company = company;
            this.industry = industry;
            this.assignedTo = assignedTo;
            this.expectedValue = expectedValue;
            this.status = status;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadDeletedEvent extends BaseEvent {
        private Long leadId;
        private String email;
        
        public LeadDeletedEvent() {
            super("lead.deleted", "sales-service");
        }
        
        public LeadDeletedEvent(Long leadId, String email) {
            super("lead.deleted", "sales-service");
            this.leadId = leadId;
            this.email = email;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadConvertedEvent extends BaseEvent {
        private Long leadId;
        private String email;
        private Long customerId;
        private Long opportunityId;
        
        public LeadConvertedEvent() {
            super("lead.converted", "sales-service");
        }
        
        public LeadConvertedEvent(Long leadId, String email, Long customerId, Long opportunityId) {
            super("lead.converted", "sales-service");
            this.leadId = leadId;
            this.email = email;
            this.customerId = customerId;
            this.opportunityId = opportunityId;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadStageChangedEvent extends BaseEvent {
        private Long leadId;
        private Long customerId;
        private String oldStage;
        private String newStage;
        private Long assignedTo;
        
        public LeadStageChangedEvent() {
            super("lead.stage.changed", "sales-service");
        }
        
        public LeadStageChangedEvent(Long leadId, Long customerId, String oldStage, String newStage, Long assignedTo) {
            super("lead.stage.changed", "sales-service");
            this.leadId = leadId;
            this.customerId = customerId;
            this.oldStage = oldStage;
            this.newStage = newStage;
            this.assignedTo = assignedTo;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadAssignedEvent extends BaseEvent {
        private Long leadId;
        private Long customerId;
        private Long oldAssignedTo;
        private Long newAssignedTo;
        
        public LeadAssignedEvent() {
            super("lead.assigned", "sales-service");
        }
        
        public LeadAssignedEvent(Long leadId, Long customerId, Long oldAssignedTo, Long newAssignedTo) {
            super("lead.assigned", "sales-service");
            this.leadId = leadId;
            this.customerId = customerId;
            this.oldAssignedTo = oldAssignedTo;
            this.newAssignedTo = newAssignedTo;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LeadClosedEvent extends BaseEvent {
        private Long leadId;
        private Long customerId;
        private String stage;
        private BigDecimal value;
        private Long assignedTo;
        
        public LeadClosedEvent() {
            super("lead.closed", "sales-service");
        }
        
        public LeadClosedEvent(Long leadId, Long customerId, String stage, BigDecimal value, Long assignedTo) {
            super("lead.closed", "sales-service");
            this.leadId = leadId;
            this.customerId = customerId;
            this.stage = stage;
            this.value = value;
            this.assignedTo = assignedTo;
        }
    }
} 