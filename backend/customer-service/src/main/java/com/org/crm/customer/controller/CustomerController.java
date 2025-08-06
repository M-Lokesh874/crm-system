package com.org.crm.customer.controller;

import com.org.crm.customer.model.Customer;
import com.org.crm.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Customer operations
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<CustomerService.CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerService.CreateCustomerRequest request) {
        log.info("Creating new customer with email: {}", request.email());
        CustomerService.CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieves a customer by their ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<CustomerService.CustomerResponse> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.debug("Fetching customer by ID: {}", id);
        Optional<CustomerService.CustomerResponse> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email", description = "Retrieves a customer by their email address")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<CustomerService.CustomerResponse> getCustomerByEmail(
            @Parameter(description = "Customer email") @PathVariable String email) {
        log.debug("Fetching customer by email: {}", email);
        Optional<CustomerService.CustomerResponse> customer = customerService.getCustomerByEmail(email);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer's information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<CustomerService.CustomerResponse> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Valid @RequestBody CustomerService.UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);
        try {
            CustomerService.CustomerResponse response = customerService.updateCustomer(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating customer: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by their ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("Deleting customer with ID: {}", id);
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting customer: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves all customers with pagination and sorting")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<Page<CustomerService.CustomerResponse>> getAllCustomers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        log.debug("Fetching all customers with pagination: page={}, size={}, sortBy={}, sortDir={}", 
                 page, size, sortBy, sortDir);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerService.CustomerResponse> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Searches customers by name, email, or company")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<Page<CustomerService.CustomerResponse>> searchCustomers(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        log.debug("Searching customers with term: {}", searchTerm);
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerService.CustomerResponse> customers = customerService.searchCustomers(searchTerm, pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status", description = "Retrieves customers filtered by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<Page<CustomerService.CustomerResponse>> getCustomersByStatus(
            @Parameter(description = "Customer status") @PathVariable Customer.CustomerStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching customers by status: {}", status);
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerService.CustomerResponse> customers = customerService.getCustomersByStatus(status, pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/assigned/{assignedTo}")
    @Operation(summary = "Get customers by assigned user", description = "Retrieves customers assigned to a specific user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<Page<CustomerService.CustomerResponse>> getCustomersByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching customers assigned to: {}", assignedTo);
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerService.CustomerResponse> customers = customerService.getCustomersByAssignedTo(assignedTo, pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/company/{company}")
    @Operation(summary = "Get customers by company", description = "Retrieves customers by company name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<List<CustomerService.CustomerResponse>> getCustomersByCompany(
            @Parameter(description = "Company name") @PathVariable String company) {
        log.debug("Fetching customers by company: {}", company);
        List<CustomerService.CustomerResponse> customers = customerService.getCustomersByCompany(company);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/source/{source}")
    @Operation(summary = "Get customers by source", description = "Retrieves customers by acquisition source")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<List<CustomerService.CustomerResponse>> getCustomersBySource(
            @Parameter(description = "Customer source") @PathVariable Customer.CustomerSource source) {
        log.debug("Fetching customers by source: {}", source);
        List<CustomerService.CustomerResponse> customers = customerService.getCustomersBySource(source);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/no-contact/{days}")
    @Operation(summary = "Get customers with no recent contact", description = "Retrieves customers with no contact in the specified number of days")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<List<CustomerService.CustomerResponse>> getCustomersWithNoRecentContact(
            @Parameter(description = "Number of days") @PathVariable int days) {
        log.debug("Fetching customers with no contact in last {} days", days);
        List<CustomerService.CustomerResponse> customers = customerService.getCustomersWithNoRecentContact(days);
        return ResponseEntity.ok(customers);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update customer status", description = "Updates the status of a customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<CustomerService.CustomerResponse> updateCustomerStatus(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam Customer.CustomerStatus status) {
        log.info("Updating customer status to {} for customer ID: {}", status, id);
        try {
            CustomerService.CustomerResponse response = customerService.updateCustomerStatus(id, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating customer status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/assignment")
    @Operation(summary = "Update customer assignment", description = "Updates the assignment of a customer to a user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<CustomerService.CustomerResponse> updateCustomerAssignment(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Parameter(description = "Assigned user") @RequestParam String assignedTo) {
        log.info("Updating customer assignment to {} for customer ID: {}", assignedTo, id);
        try {
            CustomerService.CustomerResponse response = customerService.updateCustomerAssignment(id, assignedTo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating customer assignment: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/contact")
    @Operation(summary = "Update last contact date", description = "Updates the last contact date for a customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<CustomerService.CustomerResponse> updateLastContactDate(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("Updating last contact date for customer ID: {}", id);
        try {
            CustomerService.CustomerResponse response = customerService.updateLastContactDate(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating last contact date: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get customer statistics", description = "Retrieves comprehensive customer statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<CustomerService.CustomerStatistics> getCustomerStatistics() {
        log.debug("Fetching customer statistics");
        CustomerService.CustomerStatistics statistics = customerService.getCustomerStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/top-revenue")
    @Operation(summary = "Get top customers by revenue", description = "Retrieves customers ordered by revenue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Page<CustomerService.CustomerResponse>> getTopCustomersByRevenue(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.debug("Fetching top customers by revenue");
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerService.CustomerResponse> customers = customerService.getTopCustomersByRevenue(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get customers by city", description = "Retrieves customers by city")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<List<CustomerService.CustomerResponse>> getCustomersByCity(
            @Parameter(description = "City name") @PathVariable String city) {
        log.debug("Fetching customers by city: {}", city);
        List<CustomerService.CustomerResponse> customers = customerService.getCustomersByCity(city);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/country/{country}")
    @Operation(summary = "Get customers by country", description = "Retrieves customers by country")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<List<CustomerService.CustomerResponse>> getCustomersByCountry(
            @Parameter(description = "Country name") @PathVariable String country) {
        log.debug("Fetching customers by country: {}", country);
        List<CustomerService.CustomerResponse> customers = customerService.getCustomersByCountry(country);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get customers by date range", description = "Retrieves customers created within a date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<List<CustomerService.CustomerResponse>> getCustomersByDateRange(
            @Parameter(description = "Start date (ISO format)") @RequestParam String startDate,
            @Parameter(description = "End date (ISO format)") @RequestParam String endDate) {
        log.debug("Fetching customers created between {} and {}", startDate, endDate);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<CustomerService.CustomerResponse> customers = customerService.getCustomersByDateRange(start, end);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/check-email/{email}")
    @Operation(summary = "Check if email exists", description = "Checks if a customer with the given email exists")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_REP')")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email address") @PathVariable String email) {
        log.debug("Checking if email exists: {}", email);
        boolean exists = customerService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get customer count by status", description = "Retrieves the count of customers by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Long> getCustomerCountByStatus(
            @Parameter(description = "Customer status") @PathVariable Customer.CustomerStatus status) {
        log.debug("Fetching customer count by status: {}", status);
        long count = customerService.getCustomerCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/source/{source}")
    @Operation(summary = "Get customer count by source", description = "Retrieves the count of customers by source")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Long> getCustomerCountBySource(
            @Parameter(description = "Customer source") @PathVariable Customer.CustomerSource source) {
        log.debug("Fetching customer count by source: {}", source);
        long count = customerService.getCustomerCountBySource(source);
        return ResponseEntity.ok(count);
    }
} 