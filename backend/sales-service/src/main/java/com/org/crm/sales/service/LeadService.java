package com.org.crm.sales.service;

import com.org.crm.sales.model.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Lead operations
 */
public interface LeadService {

    /**
     * Create a new lead
     */
    LeadResponse createLead(CreateLeadRequest request);

    /**
     * Get lead by ID
     */
    Optional<LeadResponse> getLeadById(Long id);

    /**
     * Get lead by email
     */
    Optional<LeadResponse> getLeadByEmail(String email);

    /**
     * Update lead
     */
    LeadResponse updateLead(Long id, UpdateLeadRequest request);

    /**
     * Delete lead
     */
    void deleteLead(Long id);

    /**
     * Get all leads with pagination
     */
    Page<LeadResponse> getAllLeads(Pageable pageable);

    /**
     * Search leads
     */
    Page<LeadResponse> searchLeads(String searchTerm, Pageable pageable);

    /**
     * Get leads by status
     */
    Page<LeadResponse> getLeadsByStatus(Lead.LeadStatus status, Pageable pageable);

    /**
     * Get leads by assigned user
     */
    Page<LeadResponse> getLeadsByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Get leads by source
     */
    List<LeadResponse> getLeadsBySource(Lead.LeadSource source);

    /**
     * Get leads by priority
     */
    List<LeadResponse> getLeadsByPriority(Lead.LeadPriority priority);

    /**
     * Get leads by company
     */
    List<LeadResponse> getLeadsByCompany(String company);

    /**
     * Get leads by industry
     */
    List<LeadResponse> getLeadsByIndustry(String industry);

    /**
     * Get active leads
     */
    List<LeadResponse> getActiveLeads();

    /**
     * Get converted leads
     */
    List<LeadResponse> getConvertedLeads();

    /**
     * Get leads with no recent contact
     */
    List<LeadResponse> getLeadsWithNoRecentContact(int days);

    /**
     * Get leads needing follow-up
     */
    List<LeadResponse> getLeadsNeedingFollowUp();

    /**
     * Update lead status
     */
    LeadResponse updateLeadStatus(Long id, Lead.LeadStatus status);

    /**
     * Update lead assignment
     */
    LeadResponse updateLeadAssignment(Long id, String assignedTo);

    /**
     * Update lead priority
     */
    LeadResponse updateLeadPriority(Long id, Lead.LeadPriority priority);

    /**
     * Update last contact date
     */
    LeadResponse updateLastContactDate(Long id);

    /**
     * Convert lead to customer
     */
    LeadResponse convertLeadToCustomer(Long id, Long customerId, Long opportunityId);

    /**
     * Get lead statistics
     */
    LeadStatistics getLeadStatistics();

    /**
     * Get top leads by expected value
     */
    Page<LeadResponse> getTopLeadsByExpectedValue(Pageable pageable);

    /**
     * Get leads by close date range
     */
    List<LeadResponse> getLeadsByCloseDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get leads by creation date range
     */
    List<LeadResponse> getLeadsByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get leads by company size
     */
    List<LeadResponse> getLeadsByCompanySize(String companySize);

    /**
     * Get leads by annual revenue range
     */
    List<LeadResponse> getLeadsByAnnualRevenueRange(BigDecimal minRevenue, BigDecimal maxRevenue);

    /**
     * Check if email exists
     */
    boolean emailExists(String email);

    /**
     * Get lead count by status
     */
    long getLeadCountByStatus(Lead.LeadStatus status);

    /**
     * Get lead count by source
     */
    long getLeadCountBySource(Lead.LeadSource source);

    /**
     * Get lead count by priority
     */
    long getLeadCountByPriority(Lead.LeadPriority priority);

    /**
     * Get total expected value by status
     */
    BigDecimal getTotalExpectedValueByStatus(Lead.LeadStatus status);

    /**
     * Get average expected value
     */
    BigDecimal getAverageExpectedValue();

    /**
     * Get conversion rate by source
     */
    double getConversionRateBySource(Lead.LeadSource source);

    /**
     * Lead response DTO
     */
    record LeadResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            String company,
            String jobTitle,
            String website,
            String industry,
            String companySize,
            BigDecimal annualRevenue,
            String description,
            String notes,
            Lead.LeadStatus status,
            Lead.LeadSource source,
            Lead.LeadPriority priority,
            String assignedTo,
            BigDecimal expectedValue,
            LocalDateTime closeDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastContactDate,
            LocalDateTime convertedAt,
            Long convertedToCustomerId,
            Long convertedToOpportunityId
    ) {
        public static LeadResponse fromLead(Lead lead) {
            return new LeadResponse(
                    lead.getId(),
                    lead.getFirstName(),
                    lead.getLastName(),
                    lead.getEmail(),
                    lead.getPhone(),
                    lead.getCompany(),
                    lead.getJobTitle(),
                    lead.getWebsite(),
                    lead.getIndustry(),
                    lead.getCompanySize(),
                    lead.getAnnualRevenue(),
                    lead.getDescription(),
                    lead.getNotes(),
                    lead.getStatus(),
                    lead.getSource(),
                    lead.getPriority(),
                    lead.getAssignedTo(),
                    lead.getExpectedValue(),
                    lead.getCloseDate(),
                    lead.getCreatedAt(),
                    lead.getUpdatedAt(),
                    lead.getLastContactDate(),
                    lead.getConvertedAt(),
                    lead.getConvertedToCustomerId(),
                    lead.getConvertedToOpportunityId()
            );
        }
    }

    /**
     * Create lead request DTO
     */
    record CreateLeadRequest(
            String firstName,
            String lastName,
            String email,
            String phone,
            String company,
            String jobTitle,
            String website,
            String industry,
            String companySize,
            BigDecimal annualRevenue,
            String description,
            String notes,
            Lead.LeadStatus status,
            Lead.LeadSource source,
            Lead.LeadPriority priority,
            String assignedTo,
            BigDecimal expectedValue,
            LocalDateTime closeDate
    ) {}

    /**
     * Update lead request DTO
     */
    record UpdateLeadRequest(
            String firstName,
            String lastName,
            String phone,
            String company,
            String jobTitle,
            String website,
            String industry,
            String companySize,
            BigDecimal annualRevenue,
            String description,
            String notes,
            Lead.LeadStatus status,
            Lead.LeadSource source,
            Lead.LeadPriority priority,
            String assignedTo,
            BigDecimal expectedValue,
            LocalDateTime closeDate
    ) {}

    /**
     * Lead statistics DTO
     */
    record LeadStatistics(
            long totalLeads,
            long newLeads,
            long contactedLeads,
            long qualifiedLeads,
            long proposalSentLeads,
            long negotiationLeads,
            long closedWonLeads,
            long closedLostLeads,
            long convertedLeads,
            long leadsByWebsite,
            long leadsByReferral,
            long leadsBySocialMedia,
            long leadsByColdCall,
            long leadsByTradeShow,
            long leadsByEmailCampaign,
            long leadsByAdvertising,
            long leadsByOther,
            long lowPriorityLeads,
            long mediumPriorityLeads,
            long highPriorityLeads,
            long urgentPriorityLeads,
            BigDecimal totalExpectedValue,
            BigDecimal averageExpectedValue
    ) {}
} 