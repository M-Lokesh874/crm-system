package com.org.crm.sales.repository;

import com.org.crm.sales.model.Lead;
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
 * Repository interface for Lead entity
 */
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    /**
     * Find lead by email
     */
    Optional<Lead> findByEmail(String email);

    /**
     * Find leads by status
     */
    List<Lead> findByStatus(Lead.LeadStatus status);

    /**
     * Find leads by assigned user
     */
    List<Lead> findByAssignedTo(String assignedTo);

    /**
     * Find leads by source
     */
    List<Lead> findBySource(Lead.LeadSource source);

    /**
     * Find leads by priority
     */
    List<Lead> findByPriority(Lead.LeadPriority priority);

    /**
     * Find leads by company
     */
    List<Lead> findByCompanyContainingIgnoreCase(String company);

    /**
     * Find leads by industry
     */
    List<Lead> findByIndustryIgnoreCase(String industry);

    /**
     * Find leads created after a specific date
     */
    List<Lead> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find leads with no contact in the last X days
     */
    @Query("SELECT l FROM Lead l WHERE l.lastContactDate IS NULL OR l.lastContactDate < :date")
    List<Lead> findLeadsWithNoRecentContact(@Param("date") LocalDateTime date);

    /**
     * Find leads that are active
     */
    @Query("SELECT l FROM Lead l WHERE l.status IN ('NEW', 'CONTACTED', 'QUALIFIED', 'PROPOSAL_SENT', 'NEGOTIATION')")
    List<Lead> findActiveLeads();

    /**
     * Find leads that are converted
     */
    @Query("SELECT l FROM Lead l WHERE l.status IN ('CONVERTED', 'CLOSED_WON')")
    List<Lead> findConvertedLeads();

    /**
     * Search leads by name, email, or company
     */
    @Query("SELECT l FROM Lead l WHERE " +
           "LOWER(l.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.company) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Lead> searchLeads(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find leads by status with pagination
     */
    Page<Lead> findByStatus(Lead.LeadStatus status, Pageable pageable);

    /**
     * Find leads by assigned user with pagination
     */
    Page<Lead> findByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Find leads by source with pagination
     */
    Page<Lead> findBySource(Lead.LeadSource source, Pageable pageable);

    /**
     * Find leads by priority with pagination
     */
    Page<Lead> findByPriority(Lead.LeadPriority priority, Pageable pageable);

    /**
     * Count leads by status
     */
    long countByStatus(Lead.LeadStatus status);

    /**
     * Count leads by source
     */
    long countBySource(Lead.LeadSource source);

    /**
     * Count leads by priority
     */
    long countByPriority(Lead.LeadPriority priority);

    /**
     * Find leads with highest expected value
     */
    @Query("SELECT l FROM Lead l ORDER BY l.expectedValue DESC")
    Page<Lead> findTopLeadsByExpectedValue(Pageable pageable);

    /**
     * Find leads by expected close date range
     */
    @Query("SELECT l FROM Lead l WHERE l.closeDate BETWEEN :startDate AND :endDate")
    List<Lead> findByCloseDateBetween(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Find leads created in date range
     */
    @Query("SELECT l FROM Lead l WHERE l.createdAt BETWEEN :startDate AND :endDate")
    List<Lead> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Find leads by company size
     */
    List<Lead> findByCompanySizeIgnoreCase(String companySize);

    /**
     * Find leads by annual revenue range
     */
    @Query("SELECT l FROM Lead l WHERE l.annualRevenue BETWEEN :minRevenue AND :maxRevenue")
    List<Lead> findByAnnualRevenueBetween(@Param("minRevenue") BigDecimal minRevenue,
                                         @Param("maxRevenue") BigDecimal maxRevenue);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find leads that need follow-up (no contact in last 7 days)
     */
    @Query("SELECT l FROM Lead l WHERE l.lastContactDate IS NULL OR l.lastContactDate < :followUpDate")
    List<Lead> findLeadsNeedingFollowUp(@Param("followUpDate") LocalDateTime followUpDate);

    /**
     * Find leads by conversion date
     */
    @Query("SELECT l FROM Lead l WHERE l.convertedAt BETWEEN :startDate AND :endDate")
    List<Lead> findByConvertedAtBetween(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Get total expected value by status
     */
    @Query("SELECT SUM(l.expectedValue) FROM Lead l WHERE l.status = :status")
    BigDecimal getTotalExpectedValueByStatus(@Param("status") Lead.LeadStatus status);

    /**
     * Get average expected value
     */
    @Query("SELECT AVG(l.expectedValue) FROM Lead l")
    BigDecimal getAverageExpectedValue();

    /**
     * Get conversion rate by source
     */
    @Query("SELECT COUNT(l) FROM Lead l WHERE l.source = :source AND l.status IN ('CONVERTED', 'CLOSED_WON')")
    long getConvertedLeadsBySource(@Param("source") Lead.LeadSource source);
} 