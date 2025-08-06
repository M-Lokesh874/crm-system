package com.org.crm.customer.repository;

import com.org.crm.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Find customers by status
     */
    List<Customer> findByStatus(Customer.CustomerStatus status);

    /**
     * Find customers by assigned user
     */
    List<Customer> findByAssignedTo(String assignedTo);

    /**
     * Find customers by company
     */
    List<Customer> findByCompanyContainingIgnoreCase(String company);

    /**
     * Find customers by source
     */
    List<Customer> findBySource(Customer.CustomerSource source);

    /**
     * Find customers created after a specific date
     */
    List<Customer> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find customers with no contact in the last X days
     */
    @Query("SELECT c FROM Customer c WHERE c.lastContactDate IS NULL OR c.lastContactDate < :date")
    List<Customer> findCustomersWithNoRecentContact(@Param("date") LocalDateTime date);

    /**
     * Search customers by name, email, or company
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.company) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> searchCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find customers by status with pagination
     */
    Page<Customer> findByStatus(Customer.CustomerStatus status, Pageable pageable);

    /**
     * Find customers by assigned user with pagination
     */
    Page<Customer> findByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Count customers by status
     */
    long countByStatus(Customer.CustomerStatus status);

    /**
     * Count customers by source
     */
    long countBySource(Customer.CustomerSource source);

    /**
     * Find VIP customers
     */
    List<Customer> findByStatusOrderByTotalRevenueDesc(Customer.CustomerStatus status);

    /**
     * Find customers with highest revenue
     */
    @Query("SELECT c FROM Customer c ORDER BY c.totalRevenue DESC")
    Page<Customer> findTopCustomersByRevenue(Pageable pageable);

    /**
     * Find customers by city
     */
    List<Customer> findByCityIgnoreCase(String city);

    /**
     * Find customers by country
     */
    List<Customer> findByCountryIgnoreCase(String country);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find customers created in date range
     */
    @Query("SELECT c FROM Customer c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Customer> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
} 