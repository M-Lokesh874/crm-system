package com.org.crm.sales.controller;

import com.org.crm.sales.model.Lead;
import com.org.crm.sales.service.LeadService;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Lead operations
 */
@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lead Management", description = "APIs for managing sales leads")
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    @Operation(summary = "Create a new lead", description = "Creates a new lead with the provided information")
    public ResponseEntity<LeadService.LeadResponse> createLead(
            @Valid @RequestBody LeadService.CreateLeadRequest request) {
        log.info("Creating new lead with email: {}", request.email());
        LeadService.LeadResponse response = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lead by ID", description = "Retrieves a lead by their ID")
    public ResponseEntity<LeadService.LeadResponse> getLeadById(
            @Parameter(description = "Lead ID") @PathVariable Long id) {
        log.debug("Fetching lead by ID: {}", id);
        Optional<LeadService.LeadResponse> lead = leadService.getLeadById(id);
        return lead.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get lead by email", description = "Retrieves a lead by their email address")
    public ResponseEntity<LeadService.LeadResponse> getLeadByEmail(
            @Parameter(description = "Lead email") @PathVariable String email) {
        log.debug("Fetching lead by email: {}", email);
        Optional<LeadService.LeadResponse> lead = leadService.getLeadByEmail(email);
        return lead.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update lead", description = "Updates an existing lead's information")
    public ResponseEntity<LeadService.LeadResponse> updateLead(
            @Parameter(description = "Lead ID") @PathVariable Long id,
            @Valid @RequestBody LeadService.UpdateLeadRequest request) {
        log.info("Updating lead with ID: {}", id);
        try {
            LeadService.LeadResponse response = leadService.updateLead(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lead", description = "Deletes a lead by their ID")
    public ResponseEntity<Void> deleteLead(
            @Parameter(description = "Lead ID") @PathVariable Long id) {
        log.info("Deleting lead with ID: {}", id);
        try {
            leadService.deleteLead(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all leads", description = "Retrieves all leads with pagination and sorting")
    public ResponseEntity<Page<LeadService.LeadResponse>> getAllLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.debug("Fetching all leads with pagination: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LeadService.LeadResponse> leads = leadService.getAllLeads(pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/search")
    @Operation(summary = "Search leads", description = "Searches leads by name, email, or company")
    public ResponseEntity<Page<LeadService.LeadResponse>> searchLeads(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Searching leads with term: {}", searchTerm);
        Pageable pageable = PageRequest.of(page, size);
        Page<LeadService.LeadResponse> leads = leadService.searchLeads(searchTerm, pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get leads by status", description = "Retrieves leads filtered by status")
    public ResponseEntity<Page<LeadService.LeadResponse>> getLeadsByStatus(
            @Parameter(description = "Lead status") @PathVariable Lead.LeadStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching leads by status: {}", status);
        Pageable pageable = PageRequest.of(page, size);
        Page<LeadService.LeadResponse> leads = leadService.getLeadsByStatus(status, pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/assigned/{assignedTo}")
    @Operation(summary = "Get leads by assigned user", description = "Retrieves leads assigned to a specific user")
    public ResponseEntity<Page<LeadService.LeadResponse>> getLeadsByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching leads assigned to: {}", assignedTo);
        Pageable pageable = PageRequest.of(page, size);
        Page<LeadService.LeadResponse> leads = leadService.getLeadsByAssignedTo(assignedTo, pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/company/{company}")
    @Operation(summary = "Get leads by company", description = "Retrieves leads by company name")
    public ResponseEntity<List<LeadService.LeadResponse>> getLeadsByCompany(
            @Parameter(description = "Company name") @PathVariable String company) {
        log.debug("Fetching leads by company: {}", company);
        List<LeadService.LeadResponse> leads = leadService.getLeadsByCompany(company);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/industry/{industry}")
    @Operation(summary = "Get leads by industry", description = "Retrieves leads by industry")
    public ResponseEntity<List<LeadService.LeadResponse>> getLeadsByIndustry(
            @Parameter(description = "Industry") @PathVariable String industry) {
        log.debug("Fetching leads by industry: {}", industry);
        List<LeadService.LeadResponse> leads = leadService.getLeadsByIndustry(industry);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/source/{source}")
    @Operation(summary = "Get leads by source", description = "Retrieves leads by acquisition source")
    public ResponseEntity<List<LeadService.LeadResponse>> getLeadsBySource(
            @Parameter(description = "Lead source") @PathVariable Lead.LeadSource source) {
        log.debug("Fetching leads by source: {}", source);
        List<LeadService.LeadResponse> leads = leadService.getLeadsBySource(source);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get leads by priority", description = "Retrieves leads by priority")
    public ResponseEntity<List<LeadService.LeadResponse>> getLeadsByPriority(
            @Parameter(description = "Lead priority") @PathVariable Lead.LeadPriority priority) {
        log.debug("Fetching leads by priority: {}", priority);
        List<LeadService.LeadResponse> leads = leadService.getLeadsByPriority(priority);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/no-contact/{days}")
    @Operation(summary = "Get leads with no recent contact", description = "Retrieves leads with no contact in the specified number of days")
    public ResponseEntity<List<LeadService.LeadResponse>> getLeadsWithNoRecentContact(
            @Parameter(description = "Number of days") @PathVariable int days) {
        log.debug("Fetching leads with no contact in last {} days", days);
        List<LeadService.LeadResponse> leads = leadService.getLeadsWithNoRecentContact(days);
        return ResponseEntity.ok(leads);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update lead status", description = "Updates the status of a lead")
    public ResponseEntity<LeadService.LeadResponse> updateLeadStatus(
            @Parameter(description = "Lead ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam Lead.LeadStatus status) {
        log.info("Updating lead status to {} for lead ID: {}", status, id);
        try {
            LeadService.LeadResponse response = leadService.updateLeadStatus(id, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating lead status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/assignment")
    @Operation(summary = "Update lead assignment", description = "Updates the assignment of a lead to a user")
    public ResponseEntity<LeadService.LeadResponse> updateLeadAssignment(
            @Parameter(description = "Lead ID") @PathVariable Long id,
            @Parameter(description = "Assigned user") @RequestParam String assignedTo) {
        log.info("Updating lead assignment to {} for lead ID: {}", assignedTo, id);
        try {
            LeadService.LeadResponse response = leadService.updateLeadAssignment(id, assignedTo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating lead assignment: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/priority")
    @Operation(summary = "Update lead priority", description = "Updates the priority of a lead")
    public ResponseEntity<LeadService.LeadResponse> updateLeadPriority(
            @Parameter(description = "Lead ID") @PathVariable Long id,
            @Parameter(description = "Priority") @RequestParam Lead.LeadPriority priority) {
        log.info("Updating lead priority to {} for lead ID: {}", priority, id);
        try {
            LeadService.LeadResponse response = leadService.updateLeadPriority(id, priority);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating lead priority: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/contact")
    @Operation(summary = "Update last contact date", description = "Updates the last contact date for a lead")
    public ResponseEntity<LeadService.LeadResponse> updateLastContactDate(
            @Parameter(description = "Lead ID") @PathVariable Long id) {
        log.info("Updating last contact date for lead ID: {}", id);
        try {
            LeadService.LeadResponse response = leadService.updateLastContactDate(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating last contact date: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/convert")
    @Operation(summary = "Convert lead to customer", description = "Converts a lead to a customer and opportunity")
    public ResponseEntity<LeadService.LeadResponse> convertLeadToCustomer(
            @Parameter(description = "Lead ID") @PathVariable Long id,
            @RequestParam Long customerId,
            @RequestParam Long opportunityId) {
        log.info("Converting lead to customer for lead ID: {}", id);
        try {
            LeadService.LeadResponse response = leadService.convertLeadToCustomer(id, customerId, opportunityId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error converting lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get lead statistics", description = "Retrieves comprehensive lead statistics")
    public ResponseEntity<LeadService.LeadStatistics> getLeadStatistics() {
        log.debug("Fetching lead statistics");
        LeadService.LeadStatistics statistics = leadService.getLeadStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/top-expected-value")
    @Operation(summary = "Get top leads by expected value", description = "Retrieves leads ordered by expected value")
    public ResponseEntity<Page<LeadService.LeadResponse>> getTopLeadsByExpectedValue(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Fetching top leads by expected value");
        Pageable pageable = PageRequest.of(page, size);
        Page<LeadService.LeadResponse> leads = leadService.getTopLeadsByExpectedValue(pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/check-email/{email}")
    @Operation(summary = "Check if email exists", description = "Checks if a lead with the given email exists")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email address") @PathVariable String email) {
        log.debug("Checking if email exists: {}", email);
        boolean exists = leadService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get lead count by status", description = "Retrieves the count of leads by status")
    public ResponseEntity<Long> getLeadCountByStatus(
            @Parameter(description = "Lead status") @PathVariable Lead.LeadStatus status) {
        log.debug("Fetching lead count by status: {}", status);
        long count = leadService.getLeadCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/source/{source}")
    @Operation(summary = "Get lead count by source", description = "Retrieves the count of leads by source")
    public ResponseEntity<Long> getLeadCountBySource(
            @Parameter(description = "Lead source") @PathVariable Lead.LeadSource source) {
        log.debug("Fetching lead count by source: {}", source);
        long count = leadService.getLeadCountBySource(source);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/priority/{priority}")
    @Operation(summary = "Get lead count by priority", description = "Retrieves the count of leads by priority")
    public ResponseEntity<Long> getLeadCountByPriority(
            @Parameter(description = "Lead priority") @PathVariable Lead.LeadPriority priority) {
        log.debug("Fetching lead count by priority: {}", priority);
        long count = leadService.getLeadCountByPriority(priority);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/conversion-rate/{source}")
    @Operation(summary = "Get conversion rate by source", description = "Retrieves the conversion rate of leads by source")
    public ResponseEntity<Double> getConversionRateBySource(
            @Parameter(description = "Lead source") @PathVariable Lead.LeadSource source) {
        log.debug("Fetching conversion rate by source: {}", source);
        double rate = leadService.getConversionRateBySource(source);
        return ResponseEntity.ok(rate);
    }
}