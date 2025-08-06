package com.org.crm.sales.controller;

import com.org.crm.sales.model.Opportunity;
import com.org.crm.sales.service.OpportunityService;
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
 * REST Controller for Opportunity operations
 */
@RestController
@RequestMapping("/api/v1/opportunities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Opportunity Management", description = "APIs for managing sales opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    @PostMapping
    @Operation(summary = "Create a new opportunity", description = "Creates a new opportunity with the provided information")
    public ResponseEntity<OpportunityService.OpportunityResponse> createOpportunity(
            @Valid @RequestBody OpportunityService.CreateOpportunityRequest request) {
        log.info("Creating new opportunity: {}", request.name());
        OpportunityService.OpportunityResponse response = opportunityService.createOpportunity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get opportunity by ID", description = "Retrieves an opportunity by its ID")
    public ResponseEntity<OpportunityService.OpportunityResponse> getOpportunityById(
            @Parameter(description = "Opportunity ID") @PathVariable Long id) {
        log.debug("Fetching opportunity by ID: {}", id);
        Optional<OpportunityService.OpportunityResponse> opportunity = opportunityService.getOpportunityById(id);
        return opportunity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get opportunity by name", description = "Retrieves an opportunity by its name")
    public ResponseEntity<OpportunityService.OpportunityResponse> getOpportunityByName(
            @Parameter(description = "Opportunity name") @PathVariable String name) {
        log.debug("Fetching opportunity by name: {}", name);
        Optional<OpportunityService.OpportunityResponse> opportunity = opportunityService.getOpportunityByName(name);
        return opportunity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update opportunity", description = "Updates an existing opportunity's information")
    public ResponseEntity<OpportunityService.OpportunityResponse> updateOpportunity(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @Valid @RequestBody OpportunityService.UpdateOpportunityRequest request) {
        log.info("Updating opportunity with ID: {}", id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.updateOpportunity(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating opportunity: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete opportunity", description = "Deletes an opportunity by its ID")
    public ResponseEntity<Void> deleteOpportunity(
            @Parameter(description = "Opportunity ID") @PathVariable Long id) {
        log.info("Deleting opportunity with ID: {}", id);
        try {
            opportunityService.deleteOpportunity(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting opportunity: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all opportunities", description = "Retrieves all opportunities with pagination and sorting")
    public ResponseEntity<Page<OpportunityService.OpportunityResponse>> getAllOpportunities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.debug("Fetching all opportunities with pagination: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OpportunityService.OpportunityResponse> opportunities = opportunityService.getAllOpportunities(pageable);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/search")
    @Operation(summary = "Search opportunities", description = "Searches opportunities by name, description, or customer name")
    public ResponseEntity<Page<OpportunityService.OpportunityResponse>> searchOpportunities(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Searching opportunities with term: {}", searchTerm);
        Pageable pageable = PageRequest.of(page, size);
        Page<OpportunityService.OpportunityResponse> opportunities = opportunityService.searchOpportunities(searchTerm, pageable);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/stage/{stage}")
    @Operation(summary = "Get opportunities by stage", description = "Retrieves opportunities filtered by stage")
    public ResponseEntity<Page<OpportunityService.OpportunityResponse>> getOpportunitiesByStage(
            @Parameter(description = "Opportunity stage") @PathVariable Opportunity.OpportunityStage stage,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching opportunities by stage: {}", stage);
        Pageable pageable = PageRequest.of(page, size);
        Page<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesByStage(stage, pageable);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/assigned/{assignedTo}")
    @Operation(summary = "Get opportunities by assigned user", description = "Retrieves opportunities assigned to a specific user")
    public ResponseEntity<Page<OpportunityService.OpportunityResponse>> getOpportunitiesByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching opportunities assigned to: {}", assignedTo);
        Pageable pageable = PageRequest.of(page, size);
        Page<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesByAssignedTo(assignedTo, pageable);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get opportunities by type", description = "Retrieves opportunities by type")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getOpportunitiesByType(
            @Parameter(description = "Opportunity type") @PathVariable Opportunity.OpportunityType type) {
        log.debug("Fetching opportunities by type: {}", type);
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesByType(type);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get opportunities by customer ID", description = "Retrieves opportunities by customer ID")
    public ResponseEntity<Page<OpportunityService.OpportunityResponse>> getOpportunitiesByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching opportunities by customer ID: {}", customerId);
        Pageable pageable = PageRequest.of(page, size);
        Page<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesByCustomerId(customerId, pageable);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/lead/{leadId}")
    @Operation(summary = "Get opportunities by lead ID", description = "Retrieves opportunities by lead ID")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getOpportunitiesByLeadId(
            @Parameter(description = "Lead ID") @PathVariable Long leadId) {
        log.debug("Fetching opportunities by lead ID: {}", leadId);
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesByLeadId(leadId);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/source/{source}")
    @Operation(summary = "Get opportunities by source", description = "Retrieves opportunities by source")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getOpportunitiesBySource(
            @Parameter(description = "Source") @PathVariable String source) {
        log.debug("Fetching opportunities by source: {}", source);
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesBySource(source);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/campaign/{campaignId}")
    @Operation(summary = "Get opportunities by campaign ID", description = "Retrieves opportunities by campaign ID")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getOpportunitiesByCampaignId(
            @Parameter(description = "Campaign ID") @PathVariable String campaignId) {
        log.debug("Fetching opportunities by campaign ID: {}", campaignId);
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getOpportunitiesByCampaignId(campaignId);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active opportunities", description = "Retrieves all active opportunities")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getActiveOpportunities() {
        log.debug("Fetching active opportunities");
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getActiveOpportunities();
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/won")
    @Operation(summary = "Get won opportunities", description = "Retrieves all won opportunities")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getWonOpportunities() {
        log.debug("Fetching won opportunities");
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getWonOpportunities();
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/lost")
    @Operation(summary = "Get lost opportunities", description = "Retrieves all lost opportunities")
    public ResponseEntity<List<OpportunityService.OpportunityResponse>> getLostOpportunities() {
        log.debug("Fetching lost opportunities");
        List<OpportunityService.OpportunityResponse> opportunities = opportunityService.getLostOpportunities();
        return ResponseEntity.ok(opportunities);
    }

    @PatchMapping("/{id}/stage")
    @Operation(summary = "Update opportunity stage", description = "Updates the stage of an opportunity")
    public ResponseEntity<OpportunityService.OpportunityResponse> updateOpportunityStage(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @Parameter(description = "New stage") @RequestParam Opportunity.OpportunityStage stage) {
        log.info("Updating opportunity stage to {} for opportunity ID: {}", stage, id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.updateOpportunityStage(id, stage);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating opportunity stage: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/assignment")
    @Operation(summary = "Update opportunity assignment", description = "Updates the assignment of an opportunity to a user")
    public ResponseEntity<OpportunityService.OpportunityResponse> updateOpportunityAssignment(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @Parameter(description = "Assigned user") @RequestParam String assignedTo) {
        log.info("Updating opportunity assignment to {} for opportunity ID: {}", assignedTo, id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.updateOpportunityAssignment(id, assignedTo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating opportunity assignment: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/probability")
    @Operation(summary = "Update opportunity probability", description = "Updates the probability of an opportunity")
    public ResponseEntity<OpportunityService.OpportunityResponse> updateOpportunityProbability(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @Parameter(description = "Probability") @RequestParam Integer probability) {
        log.info("Updating opportunity probability to {} for opportunity ID: {}", probability, id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.updateOpportunityProbability(id, probability);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating opportunity probability: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/amount")
    @Operation(summary = "Update opportunity amount", description = "Updates the amount of an opportunity")
    public ResponseEntity<OpportunityService.OpportunityResponse> updateOpportunityAmount(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @Parameter(description = "Amount") @RequestParam BigDecimal amount) {
        log.info("Updating opportunity amount to {} for opportunity ID: {}", amount, id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.updateOpportunityAmount(id, amount);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating opportunity amount: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/next-action")
    @Operation(summary = "Update next action", description = "Updates the next action and date for an opportunity")
    public ResponseEntity<OpportunityService.OpportunityResponse> updateNextAction(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @RequestParam String nextAction,
            @RequestParam String nextActionDate) {
        log.info("Updating next action for opportunity ID: {}", id);
        try {
            LocalDateTime date = LocalDateTime.parse(nextActionDate);
            OpportunityService.OpportunityResponse response = opportunityService.updateNextAction(id, nextAction, date);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating next action: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/win")
    @Operation(summary = "Win opportunity", description = "Marks an opportunity as won")
    public ResponseEntity<OpportunityService.OpportunityResponse> winOpportunity(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @RequestParam String closeReason) {
        log.info("Winning opportunity with ID: {}", id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.winOpportunity(id, closeReason);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error winning opportunity: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/lose")
    @Operation(summary = "Lose opportunity", description = "Marks an opportunity as lost")
    public ResponseEntity<OpportunityService.OpportunityResponse> loseOpportunity(
            @Parameter(description = "Opportunity ID") @PathVariable Long id,
            @RequestParam String lostReason) {
        log.info("Losing opportunity with ID: {}", id);
        try {
            OpportunityService.OpportunityResponse response = opportunityService.loseOpportunity(id, lostReason);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error losing opportunity: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get opportunity statistics", description = "Retrieves comprehensive opportunity statistics")
    public ResponseEntity<OpportunityService.OpportunityStatistics> getOpportunityStatistics() {
        log.debug("Fetching opportunity statistics");
        OpportunityService.OpportunityStatistics statistics = opportunityService.getOpportunityStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/top-amount")
    @Operation(summary = "Get top opportunities by amount", description = "Retrieves opportunities ordered by amount")
    public ResponseEntity<Page<OpportunityService.OpportunityResponse>> getTopOpportunitiesByAmount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Fetching top opportunities by amount");
        Pageable pageable = PageRequest.of(page, size);
        Page<OpportunityService.OpportunityResponse> opportunities = opportunityService.getTopOpportunitiesByAmount(pageable);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/count/stage/{stage}")
    @Operation(summary = "Get opportunity count by stage", description = "Retrieves the count of opportunities by stage")
    public ResponseEntity<Long> getOpportunityCountByStage(
            @Parameter(description = "Opportunity stage") @PathVariable Opportunity.OpportunityStage stage) {
        log.debug("Fetching opportunity count by stage: {}", stage);
        long count = opportunityService.getOpportunityCountByStage(stage);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/type/{type}")
    @Operation(summary = "Get opportunity count by type", description = "Retrieves the count of opportunities by type")
    public ResponseEntity<Long> getOpportunityCountByType(
            @Parameter(description = "Opportunity type") @PathVariable Opportunity.OpportunityType type) {
        log.debug("Fetching opportunity count by type: {}", type);
        long count = opportunityService.getOpportunityCountByType(type);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/assigned/{assignedTo}")
    @Operation(summary = "Get opportunity count by assigned user", description = "Retrieves the count of opportunities by assigned user")
    public ResponseEntity<Long> getOpportunityCountByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo) {
        log.debug("Fetching opportunity count by assigned user: {}", assignedTo);
        long count = opportunityService.getOpportunityCountByAssignedTo(assignedTo);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/win-rate/{assignedTo}")
    @Operation(summary = "Get win rate by assigned user", description = "Retrieves the win rate of opportunities by assigned user")
    public ResponseEntity<Double> getWinRateByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo) {
        log.debug("Fetching win rate by assigned user: {}", assignedTo);
        double rate = opportunityService.getWinRateByAssignedTo(assignedTo);
        return ResponseEntity.ok(rate);
    }
}