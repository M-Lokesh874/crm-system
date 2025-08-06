package com.org.crm.customer.service;

import com.org.crm.common.dto.CustomerDTO;
import com.org.crm.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Customer operations
 */
public interface CustomerService {

    /**
     * Create a new customer
     */
    CustomerResponse createCustomer(CreateCustomerRequest request);

    /**
     * Get customer by ID
     */
    Optional<CustomerResponse> getCustomerById(Long id);

    /**
     * Get customer by email
     */
    Optional<CustomerResponse> getCustomerByEmail(String email);

    /**
     * Update customer
     */
    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

    /**
     * Delete customer
     */
    void deleteCustomer(Long id);

    /**
     * Get all customers with pagination
     */
    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    /**
     * Search customers
     */
    Page<CustomerResponse> searchCustomers(String searchTerm, Pageable pageable);

    /**
     * Get customers by status
     */
    Page<CustomerResponse> getCustomersByStatus(Customer.CustomerStatus status, Pageable pageable);

    /**
     * Get customers by assigned user
     */
    Page<CustomerResponse> getCustomersByAssignedTo(String assignedTo, Pageable pageable);

    /**
     * Get customers by company
     */
    List<CustomerResponse> getCustomersByCompany(String company);

    /**
     * Get customers by source
     */
    List<CustomerResponse> getCustomersBySource(Customer.CustomerSource source);

    /**
     * Get customers with no recent contact
     */
    List<CustomerResponse> getCustomersWithNoRecentContact(int days);

    /**
     * Update customer status
     */
    CustomerResponse updateCustomerStatus(Long id, Customer.CustomerStatus status);

    /**
     * Update customer assignment
     */
    CustomerResponse updateCustomerAssignment(Long id, String assignedTo);

    /**
     * Update last contact date
     */
    CustomerResponse updateLastContactDate(Long id);

    /**
     * Get customer statistics
     */
    CustomerStatistics getCustomerStatistics();

    /**
     * Get top customers by revenue
     */
    Page<CustomerResponse> getTopCustomersByRevenue(Pageable pageable);

    /**
     * Get customers by location
     */
    List<CustomerResponse> getCustomersByCity(String city);

    List<CustomerResponse> getCustomersByCountry(String country);

    /**
     * Get customers created in date range
     */
    List<CustomerResponse> getCustomersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Check if email exists
     */
    boolean emailExists(String email);

    /**
     * Get customer count by status
     */
    long getCustomerCountByStatus(Customer.CustomerStatus status);

    /**
     * Get customer count by source
     */
    long getCustomerCountBySource(Customer.CustomerSource source);

    /**
     * Customer response DTO
     */
    record CustomerResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            String company,
            String jobTitle,
            String address,
            String city,
            String state,
            String country,
            String postalCode,
            String website,
            String notes,
            Customer.CustomerStatus status,
            Customer.CustomerSource source,
            String assignedTo,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastContactDate,
            Integer totalOrders,
            BigDecimal totalRevenue
    ) {
        public static CustomerResponse fromCustomer(Customer customer) {
            return new CustomerResponse(
                    customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCompany(),
                    customer.getJobTitle(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getState(),
                    customer.getCountry(),
                    customer.getPostalCode(),
                    customer.getWebsite(),
                    customer.getNotes(),
                    customer.getStatus(),
                    customer.getSource(),
                    customer.getAssignedTo(),
                    customer.getCreatedAt(),
                    customer.getUpdatedAt(),
                    customer.getLastContactDate(),
                    customer.getTotalOrders(),
                    customer.getTotalRevenue()
            );
        }
    }

    /**
     * Create customer request DTO
     */
    record CreateCustomerRequest(
            String firstName,
            String lastName,
            String email,
            String phone,
            String company,
            String jobTitle,
            String address,
            String city,
            String state,
            String country,
            String postalCode,
            String website,
            String notes,
            Customer.CustomerStatus status,
            Customer.CustomerSource source,
            String assignedTo
    ) {}

    /**
     * Update customer request DTO
     */
    record UpdateCustomerRequest(
            String firstName,
            String lastName,
            String phone,
            String company,
            String jobTitle,
            String address,
            String city,
            String state,
            String country,
            String postalCode,
            String website,
            String notes,
            Customer.CustomerStatus status,
            Customer.CustomerSource source,
            String assignedTo
    ) {}

    /**
     * Customer statistics DTO
     */
    record CustomerStatistics(
            long totalCustomers,
            long activeCustomers,
            long inactiveCustomers,
            long prospectCustomers,
            long leadCustomers,
            long vipCustomers,
            long customersByWebsite,
            long customersByReferral,
            long customersBySocialMedia,
            long customersByColdCall,
            long customersByTradeShow,
            long customersByOther
    ) {}
} 