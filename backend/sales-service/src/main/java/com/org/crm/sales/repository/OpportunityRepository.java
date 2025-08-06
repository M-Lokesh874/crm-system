package com.org.crm.sales.repository;

import com.org.crm.sales.model.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Opportunity entity
 */
@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    /**
     * Find opportunity by name
     */
    Optional<Opportunity> findByName(String name);

    /**
     * Check if opportunity name exists
     */
    @Query("SELECT COUNT(o) > 0 FROM Opportunity o WHERE o.name = :name")
    boolean nameExists(@Param("name") String name);

    /**
     * Find opportunities by stage
     */
    List<Opportunity> findByStage(Opportunity.OpportunityStage stage);

    /**
     * Find opportunities by assigned user
     */
    List<Opportunity> findByAssignedTo(String assignedTo);

    /**
     * Find opportunities by type
     */
    List<Opportunity> findByType(Opportunity.OpportunityType type);

    /**
     * Find opportunities by customer ID
     */
    List<Opportunity> findByCustomerId(Long customerId);

    /**
     * Find opportunities by lead ID
     */
    List<Opportunity> findByLeadId(Long leadId);

    /**
     * Find opportunities by source
     */
    List<Opportunity> findBySource(String source);

    /**
     * Find opportunities by campaign ID
     */
    List<Opportunity> findByCampaignId(String campaignId);

    /**
     * Find opportunities created after a specific date
     */
    List<Opportunity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find opportunities with no activity in the last X days
     */
    @Query("SELECT o FROM Opportunity o WHERE o.lastActivityDate IS NULL OR o.lastActivityDate < :date")
    List<Opportunity> findOpportunitiesWithNoRecentActivity(@Param("date") LocalDateTime date);

    /**
     * Find active opportunities
     */
    @Query("SELECT o FROM Opportunity o WHERE o.stage IN ('PROSPECTING', 'QUALIFICATION', 'PROPOSAL', 'NEGOTIATION')")
    List<Opportunity> findActiveOpportunities();

    /**
     * Find won opportunities
     */
    @Query("SELECT o FROM Opportunity o WHERE o.stage = 'CLOSED_WON'")
    List<Opportunity> findWonOpportunities();

    /**
     * Find lost opportunities
     */
    @Query("SELECT o FROM Opportunity o WHERE o.stage = 'CLOSED_LOST'")
    List<Opportunity> findLostOpportunities();

    /**
     * Search opportunities by name, description, or customer name
     */
    @Query("SELECT o FROM Opportunity o WHERE " +
           "LOWER(o.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(o.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Opportunity> searchOpportunities(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find opportunities by stage with pagination
     */
    Page<Opportunity> findByStage(Opportunity.OpportunityStage stage, Pageable pageable);

    /**
     * Find opportunities by assigned user with pagination
     */
    Page<Opportunity> findByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Find opportunities by type with pagination
     */
    Page<Opportunity> findByType(Opportunity.OpportunityType type, Pageable pageable);

    /**
     * Find opportunities by customer ID with pagination
     */
    Page<Opportunity> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * Count opportunities by stage
     */
    long countByStage(Opportunity.OpportunityStage stage);

    /**
     * Count opportunities by type
     */
    long countByType(Opportunity.OpportunityType type);

    /**
     * Count opportunities by assigned user
     */
    long countByAssignedTo(String assignedTo);

    /**
     * Find opportunities with highest amount
     */
    @Query("SELECT o FROM Opportunity o ORDER BY o.amount DESC")
    Page<Opportunity> findTopOpportunitiesByAmount(Pageable pageable);

    /**
     * Find opportunities by expected close date range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.expectedCloseDate BETWEEN :startDate AND :endDate")
    List<Opportunity> findByExpectedCloseDateBetween(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    /**
     * Find opportunities by actual close date range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.actualCloseDate BETWEEN :startDate AND :endDate")
    List<Opportunity> findByActualCloseDateBetween(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    /**
     * Find opportunities created in date range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Opportunity> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    /**
     * Find opportunities by probability range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.probability BETWEEN :minProbability AND :maxProbability")
    List<Opportunity> findByProbabilityBetween(@Param("minProbability") Integer minProbability,
                                              @Param("maxProbability") Integer maxProbability);

    /**
     * Find opportunities by amount range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.amount BETWEEN :minAmount AND :maxAmount")
    List<Opportunity> findByAmountBetween(@Param("minAmount") BigDecimal minAmount,
                                         @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find opportunities that need follow-up (next action date in past)
     */
    @Query("SELECT o FROM Opportunity o WHERE o.nextActionDate IS NOT NULL AND o.nextActionDate < :currentDate")
    List<Opportunity> findOpportunitiesNeedingFollowUp(@Param("currentDate") LocalDateTime currentDate);

    /**
     * Find opportunities won in date range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.wonAt BETWEEN :startDate AND :endDate")
    List<Opportunity> findByWonAtBetween(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    /**
     * Find opportunities lost in date range
     */
    @Query("SELECT o FROM Opportunity o WHERE o.lostAt BETWEEN :startDate AND :endDate")
    List<Opportunity> findByLostAtBetween(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Get total amount by stage
     */
    @Query("SELECT SUM(o.amount) FROM Opportunity o WHERE o.stage = :stage")
    BigDecimal getTotalAmountByStage(@Param("stage") Opportunity.OpportunityStage stage);

    /**
     * Get total weighted amount by stage
     */
    @Query("SELECT SUM(o.amount * o.probability / 100) FROM Opportunity o WHERE o.stage = :stage")
    BigDecimal getTotalWeightedAmountByStage(@Param("stage") Opportunity.OpportunityStage stage);

    /**
     * Get average amount
     */
    @Query("SELECT AVG(o.amount) FROM Opportunity o")
    BigDecimal getAverageAmount();

    /**
     * Get total amount for won opportunities
     */
    @Query("SELECT SUM(o.amount) FROM Opportunity o WHERE o.stage = 'CLOSED_WON'")
    BigDecimal getTotalWonAmount();

    /**
     * Get total amount for lost opportunities
     */
    @Query("SELECT SUM(o.amount) FROM Opportunity o WHERE o.stage = 'CLOSED_LOST'")
    BigDecimal getTotalLostAmount();

    /**
     * Get win rate by assigned user
     */
    @Query("SELECT COUNT(o) FROM Opportunity o WHERE o.assignedTo = :assignedTo AND o.stage = 'CLOSED_WON'")
    long getWonOpportunitiesByAssignedTo(@Param("assignedTo") String assignedTo);

    /**
     * Get total opportunities by assigned user
     */
    @Query("SELECT COUNT(o) FROM Opportunity o WHERE o.assignedTo = :assignedTo")
    long getTotalOpportunitiesByAssignedTo(@Param("assignedTo") String assignedTo);

    /**
     * Get opportunities by customer email
     */
    List<Opportunity> findByCustomerEmail(String customerEmail);

    /**
     * Get opportunities by next action
     */
    List<Opportunity> findByNextAction(String nextAction);
} 