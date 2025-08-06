package com.org.crm.sales.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Opportunity entity for the CRM Sales system
 */
@Entity
@Table(name = "opportunities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Opportunity name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Amount is required")
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "probability")
    @Builder.Default
    private Integer probability = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false)
    @Builder.Default
    private OpportunityStage stage = OpportunityStage.PROSPECTING;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OpportunityType type;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "expected_close_date")
    private LocalDateTime expectedCloseDate;

    @Column(name = "actual_close_date")
    private LocalDateTime actualCloseDate;

    @Column(name = "close_reason")
    private String closeReason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "source")
    private String source;

    @Column(name = "campaign_id")
    private String campaignId;

    @Column(name = "next_action")
    private String nextAction;

    @Column(name = "next_action_date")
    private LocalDateTime nextActionDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;

    @Column(name = "won_at")
    private LocalDateTime wonAt;

    @Column(name = "lost_at")
    private LocalDateTime lostAt;

    @Column(name = "lost_reason")
    private String lostReason;

    /**
     * Opportunity stage enumeration
     */
    public enum OpportunityStage {
        PROSPECTING, QUALIFICATION, PROPOSAL, NEGOTIATION, CLOSED_WON, CLOSED_LOST
    }

    /**
     * Opportunity type enumeration
     */
    public enum OpportunityType {
        NEW_BUSINESS, EXISTING_BUSINESS, RENEWAL, UPSELL, CROSS_SELL
    }

    /**
     * Check if opportunity is active
     */
    public boolean isActive() {
        return OpportunityStage.PROSPECTING.equals(stage) ||
               OpportunityStage.QUALIFICATION.equals(stage) ||
               OpportunityStage.PROPOSAL.equals(stage) ||
               OpportunityStage.NEGOTIATION.equals(stage);
    }

    /**
     * Check if opportunity is won
     */
    public boolean isWon() {
        return OpportunityStage.CLOSED_WON.equals(stage);
    }

    /**
     * Check if opportunity is lost
     */
    public boolean isLost() {
        return OpportunityStage.CLOSED_LOST.equals(stage);
    }

    /**
     * Check if opportunity is closed
     */
    public boolean isClosed() {
        return OpportunityStage.CLOSED_WON.equals(stage) ||
               OpportunityStage.CLOSED_LOST.equals(stage);
    }

    /**
     * Get weighted amount based on probability
     */
    public BigDecimal getWeightedAmount() {
        if (probability == null || probability == 0) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(BigDecimal.valueOf(probability)).divide(BigDecimal.valueOf(100));
    }
} 