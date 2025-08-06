package com.org.crm.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Opportunity-related events for the CRM system
 */
public class OpportunityEvents {
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OpportunityCreatedEvent extends BaseEvent {
        private Long opportunityId;
        private String name;
        private String customerId;
        private String leadId;
        private String assignedTo;
        private BigDecimal amount;
        private String stage;
        private String type;
        
        public OpportunityCreatedEvent() {
            super("opportunity.created", "sales-service");
        }
        
        public OpportunityCreatedEvent(Long opportunityId, String name, String customerId, String leadId, 
                                      String assignedTo, BigDecimal amount, String stage, String type) {
            super("opportunity.created", "sales-service");
            this.opportunityId = opportunityId;
            this.name = name;
            this.customerId = customerId;
            this.leadId = leadId;
            this.assignedTo = assignedTo;
            this.amount = amount;
            this.stage = stage;
            this.type = type;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OpportunityUpdatedEvent extends BaseEvent {
        private Long opportunityId;
        private String name;
        private String customerId;
        private String leadId;
        private String assignedTo;
        private BigDecimal amount;
        private String stage;
        private String type;
        
        public OpportunityUpdatedEvent() {
            super("opportunity.updated", "sales-service");
        }
        
        public OpportunityUpdatedEvent(Long opportunityId, String name, String customerId, String leadId, 
                                      String assignedTo, BigDecimal amount, String stage, String type) {
            super("opportunity.updated", "sales-service");
            this.opportunityId = opportunityId;
            this.name = name;
            this.customerId = customerId;
            this.leadId = leadId;
            this.assignedTo = assignedTo;
            this.amount = amount;
            this.stage = stage;
            this.type = type;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OpportunityDeletedEvent extends BaseEvent {
        private Long opportunityId;
        private String name;
        
        public OpportunityDeletedEvent() {
            super("opportunity.deleted", "sales-service");
        }
        
        public OpportunityDeletedEvent(Long opportunityId, String name) {
            super("opportunity.deleted", "sales-service");
            this.opportunityId = opportunityId;
            this.name = name;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OpportunityWonEvent extends BaseEvent {
        private Long opportunityId;
        private String name;
        private BigDecimal amount;
        private String customerId;
        
        public OpportunityWonEvent() {
            super("opportunity.won", "sales-service");
        }
        
        public OpportunityWonEvent(Long opportunityId, String name, BigDecimal amount, String customerId) {
            super("opportunity.won", "sales-service");
            this.opportunityId = opportunityId;
            this.name = name;
            this.amount = amount;
            this.customerId = customerId;
        }
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OpportunityLostEvent extends BaseEvent {
        private Long opportunityId;
        private String name;
        private String reason;
        private String customerId;
        
        public OpportunityLostEvent() {
            super("opportunity.lost", "sales-service");
        }
        
        public OpportunityLostEvent(Long opportunityId, String name, String reason, String customerId) {
            super("opportunity.lost", "sales-service");
            this.opportunityId = opportunityId;
            this.name = name;
            this.reason = reason;
            this.customerId = customerId;
        }
    }
} 