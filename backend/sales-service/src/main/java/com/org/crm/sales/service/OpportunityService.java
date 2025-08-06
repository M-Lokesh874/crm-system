package com.org.crm.sales.service;

import com.org.crm.sales.model.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Opportunity operations
 */
public interface OpportunityService {

    /**
     * Create a new opportunity
     */
    OpportunityResponse createOpportunity(CreateOpportunityRequest request);

    /**
     * Get opportunity by ID
     */
    Optional<OpportunityResponse> getOpportunityById(Long id);

    /**
     * Get opportunity by name
     */
    Optional<OpportunityResponse> getOpportunityByName(String name);

    /**
     * Update opportunity
     */
    OpportunityResponse updateOpportunity(Long id, UpdateOpportunityRequest request);

    /**
     * Delete opportunity
     */
    void deleteOpportunity(Long id);

    /**
     * Get all opportunities with pagination
     */
    Page<OpportunityResponse> getAllOpportunities(Pageable pageable);

    /**
     * Search opportunities
     */
    Page<OpportunityResponse> searchOpportunities(String searchTerm, Pageable pageable);

    /**
     * Get opportunities by stage
     */
    Page<OpportunityResponse> getOpportunitiesByStage(Opportunity.OpportunityStage stage, Pageable pageable);

    /**
     * Get opportunities by assigned user
     */
    Page<OpportunityResponse> getOpportunitiesByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Get opportunities by type
     */
    List<OpportunityResponse> getOpportunitiesByType(Opportunity.OpportunityType type);

    /**
     * Get opportunities by customer ID
     */
    Page<OpportunityResponse> getOpportunitiesByCustomerId(Long customerId, Pageable pageable);

    /**
     * Get opportunities by lead ID
     */
    List<OpportunityResponse> getOpportunitiesByLeadId(Long leadId);

    /**
     * Get opportunities by source
     */
    List<OpportunityResponse> getOpportunitiesBySource(String source);

    /**
     * Get opportunities by campaign ID
     */
    List<OpportunityResponse> getOpportunitiesByCampaignId(String campaignId);

    /**
     * Get active opportunities
     */
    List<OpportunityResponse> getActiveOpportunities();

    /**
     * Get won opportunities
     */
    List<OpportunityResponse> getWonOpportunities();

    /**
     * Get lost opportunities
     */
    List<OpportunityResponse> getLostOpportunities();

    /**
     * Get opportunities with no recent activity
     */
    List<OpportunityResponse> getOpportunitiesWithNoRecentActivity(int days);

    /**
     * Get opportunities needing follow-up
     */
    List<OpportunityResponse> getOpportunitiesNeedingFollowUp();

    /**
     * Update opportunity stage
     */
    OpportunityResponse updateOpportunityStage(Long id, Opportunity.OpportunityStage stage);

    /**
     * Update opportunity assignment
     */
    OpportunityResponse updateOpportunityAssignment(Long id, String assignedTo);

    /**
     * Update opportunity probability
     */
    OpportunityResponse updateOpportunityProbability(Long id, Integer probability);

    /**
     * Update opportunity amount
     */
    OpportunityResponse updateOpportunityAmount(Long id, BigDecimal amount);

    /**
     * Update next action
     */
    OpportunityResponse updateNextAction(Long id, String nextAction, LocalDateTime nextActionDate);

    /**
     * Win opportunity
     */
    OpportunityResponse winOpportunity(Long id, String closeReason);

    /**
     * Lose opportunity
     */
    OpportunityResponse loseOpportunity(Long id, String lostReason);

    /**
     * Get opportunity statistics
     */
    OpportunityStatistics getOpportunityStatistics();

    /**
     * Get top opportunities by amount
     */
    Page<OpportunityResponse> getTopOpportunitiesByAmount(Pageable pageable);

    /**
     * Get opportunities by expected close date range
     */
    List<OpportunityResponse> getOpportunitiesByExpectedCloseDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get opportunities by actual close date range
     */
    List<OpportunityResponse> getOpportunitiesByActualCloseDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get opportunities by creation date range
     */
    List<OpportunityResponse> getOpportunitiesByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get opportunities by probability range
     */
    List<OpportunityResponse> getOpportunitiesByProbabilityRange(Integer minProbability, Integer maxProbability);

    /**
     * Get opportunities by amount range
     */
    List<OpportunityResponse> getOpportunitiesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * Get opportunities by customer email
     */
    List<OpportunityResponse> getOpportunitiesByCustomerEmail(String customerEmail);

    /**
     * Get opportunities by next action
     */
    List<OpportunityResponse> getOpportunitiesByNextAction(String nextAction);

    /**
     * Check if name exists
     */
    boolean nameExists(String name);

    /**
     * Get opportunity count by stage
     */
    long getOpportunityCountByStage(Opportunity.OpportunityStage stage);

    /**
     * Get opportunity count by type
     */
    long getOpportunityCountByType(Opportunity.OpportunityType type);

    /**
     * Get opportunity count by assigned user
     */
    long getOpportunityCountByAssignedTo(String assignedTo);

    /**
     * Get total amount by stage
     */
    BigDecimal getTotalAmountByStage(Opportunity.OpportunityStage stage);

    /**
     * Get total weighted amount by stage
     */
    BigDecimal getTotalWeightedAmountByStage(Opportunity.OpportunityStage stage);

    /**
     * Get average amount
     */
    BigDecimal getAverageAmount();

    /**
     * Get total won amount
     */
    BigDecimal getTotalWonAmount();

    /**
     * Get total lost amount
     */
    BigDecimal getTotalLostAmount();

    /**
     * Get win rate by assigned user
     */
    double getWinRateByAssignedTo(String assignedTo);

    /**
     * Opportunity response DTO
     */
    record OpportunityResponse(
            Long id,
            String name,
            String description,
            BigDecimal amount,
            Integer probability,
            Opportunity.OpportunityStage stage,
            Opportunity.OpportunityType type,
            Long customerId,
            String customerName,
            String customerEmail,
            Long leadId,
            String assignedTo,
            LocalDateTime expectedCloseDate,
            LocalDateTime actualCloseDate,
            String closeReason,
            String notes,
            String source,
            String campaignId,
            String nextAction,
            LocalDateTime nextActionDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastActivityDate,
            LocalDateTime wonAt,
            LocalDateTime lostAt,
            String lostReason
    ) {
        public static OpportunityResponse fromOpportunity(Opportunity opportunity) {
            return new OpportunityResponse(
                    opportunity.getId(),
                    opportunity.getName(),
                    opportunity.getDescription(),
                    opportunity.getAmount(),
                    opportunity.getProbability(),
                    opportunity.getStage(),
                    opportunity.getType(),
                    opportunity.getCustomerId(),
                    opportunity.getCustomerName(),
                    opportunity.getCustomerEmail(),
                    opportunity.getLeadId(),
                    opportunity.getAssignedTo(),
                    opportunity.getExpectedCloseDate(),
                    opportunity.getActualCloseDate(),
                    opportunity.getCloseReason(),
                    opportunity.getNotes(),
                    opportunity.getSource(),
                    opportunity.getCampaignId(),
                    opportunity.getNextAction(),
                    opportunity.getNextActionDate(),
                    opportunity.getCreatedAt(),
                    opportunity.getUpdatedAt(),
                    opportunity.getLastActivityDate(),
                    opportunity.getWonAt(),
                    opportunity.getLostAt(),
                    opportunity.getLostReason()
            );
        }
    }

    /**
     * Create opportunity request DTO
     */
    record CreateOpportunityRequest(
            String name,
            String description,
            BigDecimal amount,
            Integer probability,
            Opportunity.OpportunityStage stage,
            Opportunity.OpportunityType type,
            Long customerId,
            String customerName,
            String customerEmail,
            Long leadId,
            String assignedTo,
            LocalDateTime expectedCloseDate,
            String notes,
            String source,
            String campaignId,
            String nextAction,
            LocalDateTime nextActionDate
    ) {}

    /**
     * Update opportunity request DTO
     */
    record UpdateOpportunityRequest(
            String name,
            String description,
            BigDecimal amount,
            Integer probability,
            Opportunity.OpportunityStage stage,
            Opportunity.OpportunityType type,
            Long customerId,
            String customerName,
            String customerEmail,
            Long leadId,
            String assignedTo,
            LocalDateTime expectedCloseDate,
            String notes,
            String source,
            String campaignId,
            String nextAction,
            LocalDateTime nextActionDate
    ) {}

    /**
     * Opportunity statistics DTO
     */
    record OpportunityStatistics(
            long totalOpportunities,
            long prospectingOpportunities,
            long qualificationOpportunities,
            long proposalOpportunities,
            long negotiationOpportunities,
            long closedWonOpportunities,
            long closedLostOpportunities,
            long newBusinessOpportunities,
            long existingBusinessOpportunities,
            long renewalOpportunities,
            long upsellOpportunities,
            long crossSellOpportunities,
            BigDecimal totalAmount,
            BigDecimal totalWeightedAmount,
            BigDecimal averageAmount,
            BigDecimal totalWonAmount,
            BigDecimal totalLostAmount,
            double winRate
    ) {}
} 