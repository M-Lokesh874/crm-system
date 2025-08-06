package com.org.crm.sales.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
 * Lead entity for the CRM Sales system
 */
@Entity
@Table(name = "leads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "company")
    private String company;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "website")
    private String website;

    @Column(name = "industry")
    private String industry;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "annual_revenue")
    private BigDecimal annualRevenue;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private LeadStatus status = LeadStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private LeadSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Builder.Default
    private LeadPriority priority = LeadPriority.MEDIUM;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "expected_value", precision = 10, scale = 2)
    private BigDecimal expectedValue;

    @Column(name = "close_date")
    private LocalDateTime closeDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_contact_date")
    private LocalDateTime lastContactDate;

    @Column(name = "converted_at")
    private LocalDateTime convertedAt;

    @Column(name = "converted_to_customer_id")
    private Long convertedToCustomerId;

    @Column(name = "converted_to_opportunity_id")
    private Long convertedToOpportunityId;

    /**
     * Lead status enumeration
     */
    public enum LeadStatus {
        NEW, CONTACTED, QUALIFIED, PROPOSAL_SENT, NEGOTIATION, CLOSED_WON, CLOSED_LOST, CONVERTED
    }

    /**
     * Lead source enumeration
     */
    public enum LeadSource {
        WEBSITE, REFERRAL, SOCIAL_MEDIA, COLD_CALL, TRADE_SHOW, EMAIL_CAMPAIGN, ADVERTISING, OTHER
    }

    /**
     * Lead priority enumeration
     */
    public enum LeadPriority {
        LOW, MEDIUM, HIGH, URGENT
    }

    /**
     * Get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Check if lead is active
     */
    public boolean isActive() {
        return LeadStatus.NEW.equals(status) ||
               LeadStatus.CONTACTED.equals(status) ||
               LeadStatus.QUALIFIED.equals(status) ||
               LeadStatus.PROPOSAL_SENT.equals(status) ||
               LeadStatus.NEGOTIATION.equals(status);
    }

    /**
     * Check if lead is converted
     */
    public boolean isConverted() {
        return LeadStatus.CONVERTED.equals(status) ||
               LeadStatus.CLOSED_WON.equals(status);
    }

    /**
     * Check if lead is closed
     */
    public boolean isClosed() {
        return LeadStatus.CLOSED_WON.equals(status) ||
               LeadStatus.CLOSED_LOST.equals(status) ||
               LeadStatus.CONVERTED.equals(status);
    }
} 