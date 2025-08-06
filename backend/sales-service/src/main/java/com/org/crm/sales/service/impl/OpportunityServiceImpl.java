package com.org.crm.sales.service.impl;

import com.org.crm.common.events.BaseEvent;
import com.org.crm.common.events.OpportunityEvents;
import com.org.crm.common.events.EventPublisher;
import com.org.crm.sales.exception.GlobalExceptionHandler;
import com.org.crm.sales.model.Opportunity;
import com.org.crm.sales.repository.OpportunityRepository;
import com.org.crm.sales.service.OpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of OpportunityService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final EventPublisher eventPublisher;

    @Override
    public OpportunityResponse createOpportunity(CreateOpportunityRequest request) {
        log.info("Creating new opportunity: {}", request.name());

        // Check if name already exists
        if (opportunityRepository.nameExists(request.name())) {
            throw new GlobalExceptionHandler.OpportunityAlreadyExistsException("Opportunity with name " + request.name() + " already exists");
        }

        // Create opportunity entity
        Opportunity opportunity = Opportunity.builder()
                .name(request.name())
                .description(request.description())
                .amount(request.amount())
                .probability(request.probability() != null ? request.probability() : 0)
                .stage(request.stage() != null ? request.stage() : Opportunity.OpportunityStage.PROSPECTING)
                .type(request.type())
                .customerId(request.customerId())
                .customerName(request.customerName())
                .customerEmail(request.customerEmail())
                .leadId(request.leadId())
                .assignedTo(request.assignedTo())
                .expectedCloseDate(request.expectedCloseDate())
                .notes(request.notes())
                .source(request.source())
                .campaignId(request.campaignId())
                .nextAction(request.nextAction())
                .nextActionDate(request.nextActionDate())
                .build();

        // Save opportunity
        Opportunity savedOpportunity = opportunityRepository.save(opportunity);
        log.info("Opportunity created successfully with ID: {}", savedOpportunity.getId());

        // Publish opportunity created event
        BaseEvent event = new OpportunityEvents.OpportunityCreatedEvent(
                savedOpportunity.getId(),
                savedOpportunity.getName(),
                savedOpportunity.getCustomerId() != null ? savedOpportunity.getCustomerId().toString() : null,
                savedOpportunity.getLeadId() != null ? savedOpportunity.getLeadId().toString() : null,
                savedOpportunity.getAssignedTo(),
                savedOpportunity.getAmount(),
                savedOpportunity.getStage().toString(),
                savedOpportunity.getType().toString()
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(savedOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OpportunityResponse> getOpportunityById(Long id) {
        log.debug("Fetching opportunity by ID: {}", id);
        return opportunityRepository.findById(id)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OpportunityResponse> getOpportunityByName(String name) {
        log.debug("Fetching opportunity by name: {}", name);
        return opportunityRepository.findByName(name)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    public OpportunityResponse updateOpportunity(Long id, UpdateOpportunityRequest request) {
        log.info("Updating opportunity with ID: {}", id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        // Update opportunity fields
        if (request.name() != null) opportunity.setName(request.name());
        if (request.description() != null) opportunity.setDescription(request.description());
        if (request.amount() != null) opportunity.setAmount(request.amount());
        if (request.probability() != null) opportunity.setProbability(request.probability());
        if (request.stage() != null) opportunity.setStage(request.stage());
        if (request.type() != null) opportunity.setType(request.type());
        if (request.customerId() != null) opportunity.setCustomerId(request.customerId());
        if (request.customerName() != null) opportunity.setCustomerName(request.customerName());
        if (request.customerEmail() != null) opportunity.setCustomerEmail(request.customerEmail());
        if (request.leadId() != null) opportunity.setLeadId(request.leadId());
        if (request.assignedTo() != null) opportunity.setAssignedTo(request.assignedTo());
        if (request.expectedCloseDate() != null) opportunity.setExpectedCloseDate(request.expectedCloseDate());
        if (request.notes() != null) opportunity.setNotes(request.notes());
        if (request.source() != null) opportunity.setSource(request.source());
        if (request.campaignId() != null) opportunity.setCampaignId(request.campaignId());
        if (request.nextAction() != null) opportunity.setNextAction(request.nextAction());
        if (request.nextActionDate() != null) opportunity.setNextActionDate(request.nextActionDate());

        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);
        log.info("Opportunity updated successfully with ID: {}", updatedOpportunity.getId());

        // Publish opportunity updated event
        BaseEvent event = new OpportunityEvents.OpportunityUpdatedEvent(
                updatedOpportunity.getId(),
                updatedOpportunity.getName(),
                updatedOpportunity.getCustomerId() != null ? updatedOpportunity.getCustomerId().toString() : null,
                updatedOpportunity.getLeadId() != null ? updatedOpportunity.getLeadId().toString() : null,
                updatedOpportunity.getAssignedTo(),
                updatedOpportunity.getAmount(),
                updatedOpportunity.getStage().toString(),
                updatedOpportunity.getType().toString()
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(updatedOpportunity);
    }

    @Override
    public void deleteOpportunity(Long id) {
        log.info("Deleting opportunity with ID: {}", id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        // Publish opportunity deleted event before deletion
        BaseEvent event = new OpportunityEvents.OpportunityDeletedEvent(
                opportunity.getId(),
                opportunity.getName()
        );
        eventPublisher.publishOpportunityEvent(event);

        opportunityRepository.deleteById(id);
        log.info("Opportunity deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getAllOpportunities(Pageable pageable) {
        log.debug("Fetching all opportunities with pagination: {}", pageable);
        return opportunityRepository.findAll(pageable)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> searchOpportunities(String searchTerm, Pageable pageable) {
        log.debug("Searching opportunities with term: {}", searchTerm);
        return opportunityRepository.searchOpportunities(searchTerm, pageable)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getOpportunitiesByStage(Opportunity.OpportunityStage stage, Pageable pageable) {
        log.debug("Fetching opportunities by stage: {}", stage);
        return opportunityRepository.findByStage(stage, pageable)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getOpportunitiesByAssignedTo(String assignedTo, Pageable pageable) {
        log.debug("Fetching opportunities assigned to: {}", assignedTo);
        return opportunityRepository.findByAssignedTo(assignedTo, pageable)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByType(Opportunity.OpportunityType type) {
        log.debug("Fetching opportunities by type: {}", type);
        return opportunityRepository.findByType(type)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getActiveOpportunities() {
        log.debug("Fetching active opportunities");
        return opportunityRepository.findActiveOpportunities()
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getWonOpportunities() {
        log.debug("Fetching won opportunities");
        return opportunityRepository.findWonOpportunities()
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getLostOpportunities() {
        log.debug("Fetching lost opportunities");
        return opportunityRepository.findLostOpportunities()
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    public OpportunityResponse updateOpportunityStage(Long id, Opportunity.OpportunityStage stage) {
        log.info("Updating opportunity stage to {} for opportunity ID: {}", stage, id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setStage(stage);
        opportunity.setLastActivityDate(LocalDateTime.now());
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);

        // Publish opportunity updated event
        BaseEvent event = new OpportunityEvents.OpportunityUpdatedEvent(
                updatedOpportunity.getId(),
                updatedOpportunity.getName(),
                updatedOpportunity.getCustomerId() != null ? updatedOpportunity.getCustomerId().toString() : null,
                updatedOpportunity.getLeadId() != null ? updatedOpportunity.getLeadId().toString() : null,
                updatedOpportunity.getAssignedTo(),
                updatedOpportunity.getAmount(),
                updatedOpportunity.getStage().toString(),
                updatedOpportunity.getType().toString()
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(updatedOpportunity);
    }

    @Override
    public OpportunityResponse updateOpportunityAssignment(Long id, String assignedTo) {
        log.info("Updating opportunity assignment to {} for opportunity ID: {}", assignedTo, id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setAssignedTo(assignedTo);
        opportunity.setLastActivityDate(LocalDateTime.now());
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);

        // Publish opportunity updated event
        BaseEvent event = new OpportunityEvents.OpportunityUpdatedEvent(
                updatedOpportunity.getId(),
                updatedOpportunity.getName(),
                updatedOpportunity.getCustomerId() != null ? updatedOpportunity.getCustomerId().toString() : null,
                updatedOpportunity.getLeadId() != null ? updatedOpportunity.getLeadId().toString() : null,
                updatedOpportunity.getAssignedTo(),
                updatedOpportunity.getAmount(),
                updatedOpportunity.getStage().toString(),
                updatedOpportunity.getType().toString()
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(updatedOpportunity);
    }

    @Override
    public OpportunityResponse updateOpportunityProbability(Long id, Integer probability) {
        log.info("Updating opportunity probability to {} for opportunity ID: {}", probability, id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setProbability(probability);
        opportunity.setLastActivityDate(LocalDateTime.now());
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);

        // Publish opportunity updated event
        BaseEvent event = new OpportunityEvents.OpportunityUpdatedEvent(
                updatedOpportunity.getId(),
                updatedOpportunity.getName(),
                updatedOpportunity.getCustomerId() != null ? updatedOpportunity.getCustomerId().toString() : null,
                updatedOpportunity.getLeadId() != null ? updatedOpportunity.getLeadId().toString() : null,
                updatedOpportunity.getAssignedTo(),
                updatedOpportunity.getAmount(),
                updatedOpportunity.getStage().toString(),
                updatedOpportunity.getType().toString()
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(updatedOpportunity);
    }

    @Override
    public OpportunityResponse updateOpportunityAmount(Long id, BigDecimal amount) {
        log.info("Updating opportunity amount to {} for opportunity ID: {}", amount, id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setAmount(amount);
        opportunity.setLastActivityDate(LocalDateTime.now());
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);

        // Publish opportunity updated event
        BaseEvent event = new OpportunityEvents.OpportunityUpdatedEvent(
                updatedOpportunity.getId(),
                updatedOpportunity.getName(),
                updatedOpportunity.getCustomerId() != null ? updatedOpportunity.getCustomerId().toString() : null,
                updatedOpportunity.getLeadId() != null ? updatedOpportunity.getLeadId().toString() : null,
                updatedOpportunity.getAssignedTo(),
                updatedOpportunity.getAmount(),
                updatedOpportunity.getStage().toString(),
                updatedOpportunity.getType().toString()
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(updatedOpportunity);
    }

    @Override
    public OpportunityResponse updateNextAction(Long id, String nextAction, LocalDateTime nextActionDate) {
        log.info("Updating next action for opportunity ID: {}", id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setNextAction(nextAction);
        opportunity.setNextActionDate(nextActionDate);
        opportunity.setLastActivityDate(LocalDateTime.now());
        Opportunity updatedOpportunity = opportunityRepository.save(opportunity);

        return OpportunityResponse.fromOpportunity(updatedOpportunity);
    }

    @Override
    public OpportunityResponse winOpportunity(Long id, String closeReason) {
        log.info("Winning opportunity with ID: {}", id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setStage(Opportunity.OpportunityStage.CLOSED_WON);
        opportunity.setActualCloseDate(LocalDateTime.now());
        opportunity.setCloseReason(closeReason);
        opportunity.setWonAt(LocalDateTime.now());
        opportunity.setLastActivityDate(LocalDateTime.now());

        Opportunity wonOpportunity = opportunityRepository.save(opportunity);

        // Publish opportunity won event
        BaseEvent event = new OpportunityEvents.OpportunityWonEvent(
                wonOpportunity.getId(),
                wonOpportunity.getName(),
                wonOpportunity.getAmount(),
                wonOpportunity.getCustomerId() != null ? wonOpportunity.getCustomerId().toString() : null
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(wonOpportunity);
    }

    @Override
    public OpportunityResponse loseOpportunity(Long id, String lostReason) {
        log.info("Losing opportunity with ID: {}", id);

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.OpportunityNotFoundException("Opportunity not found with ID: " + id));

        opportunity.setStage(Opportunity.OpportunityStage.CLOSED_LOST);
        opportunity.setActualCloseDate(LocalDateTime.now());
        opportunity.setLostReason(lostReason);
        opportunity.setLostAt(LocalDateTime.now());
        opportunity.setLastActivityDate(LocalDateTime.now());

        Opportunity lostOpportunity = opportunityRepository.save(opportunity);

        // Publish opportunity lost event
        BaseEvent event = new OpportunityEvents.OpportunityLostEvent(
                lostOpportunity.getId(),
                lostOpportunity.getName(),
                lostReason,
                lostOpportunity.getCustomerId() != null ? lostOpportunity.getCustomerId().toString() : null
        );
        eventPublisher.publishOpportunityEvent(event);

        return OpportunityResponse.fromOpportunity(lostOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public OpportunityStatistics getOpportunityStatistics() {
        log.debug("Fetching opportunity statistics");

        long totalOpportunities = opportunityRepository.count();
        long prospectingOpportunities = opportunityRepository.countByStage(Opportunity.OpportunityStage.PROSPECTING);
        long qualificationOpportunities = opportunityRepository.countByStage(Opportunity.OpportunityStage.QUALIFICATION);
        long proposalOpportunities = opportunityRepository.countByStage(Opportunity.OpportunityStage.PROPOSAL);
        long negotiationOpportunities = opportunityRepository.countByStage(Opportunity.OpportunityStage.NEGOTIATION);
        long closedWonOpportunities = opportunityRepository.countByStage(Opportunity.OpportunityStage.CLOSED_WON);
        long closedLostOpportunities = opportunityRepository.countByStage(Opportunity.OpportunityStage.CLOSED_LOST);

        long newBusinessOpportunities = opportunityRepository.countByType(Opportunity.OpportunityType.NEW_BUSINESS);
        long existingBusinessOpportunities = opportunityRepository.countByType(Opportunity.OpportunityType.EXISTING_BUSINESS);
        long renewalOpportunities = opportunityRepository.countByType(Opportunity.OpportunityType.RENEWAL);
        long upsellOpportunities = opportunityRepository.countByType(Opportunity.OpportunityType.UPSELL);
        long crossSellOpportunities = opportunityRepository.countByType(Opportunity.OpportunityType.CROSS_SELL);

        BigDecimal totalAmount = opportunityRepository.getTotalAmountByStage(null);
        BigDecimal totalWeightedAmount = opportunityRepository.getTotalWeightedAmountByStage(null);
        BigDecimal averageAmount = opportunityRepository.getAverageAmount();
        BigDecimal totalWonAmount = opportunityRepository.getTotalWonAmount();
        BigDecimal totalLostAmount = opportunityRepository.getTotalLostAmount();

        double winRate = totalOpportunities > 0 ? (double) closedWonOpportunities / totalOpportunities * 100 : 0.0;

        return new OpportunityStatistics(
                totalOpportunities,
                prospectingOpportunities,
                qualificationOpportunities,
                proposalOpportunities,
                negotiationOpportunities,
                closedWonOpportunities,
                closedLostOpportunities,
                newBusinessOpportunities,
                existingBusinessOpportunities,
                renewalOpportunities,
                upsellOpportunities,
                crossSellOpportunities,
                totalAmount != null ? totalAmount : BigDecimal.ZERO,
                totalWeightedAmount != null ? totalWeightedAmount : BigDecimal.ZERO,
                averageAmount != null ? averageAmount : BigDecimal.ZERO,
                totalWonAmount != null ? totalWonAmount : BigDecimal.ZERO,
                totalLostAmount != null ? totalLostAmount : BigDecimal.ZERO,
                winRate
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getTopOpportunitiesByAmount(Pageable pageable) {
        log.debug("Fetching top opportunities by amount");
        return opportunityRepository.findTopOpportunitiesByAmount(pageable)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nameExists(String name) {
        return opportunityRepository.nameExists(name);
    }

    @Override
    @Transactional(readOnly = true)
    public long getOpportunityCountByStage(Opportunity.OpportunityStage stage) {
        return opportunityRepository.countByStage(stage);
    }

    @Override
    @Transactional(readOnly = true)
    public long getOpportunityCountByType(Opportunity.OpportunityType type) {
        return opportunityRepository.countByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public long getOpportunityCountByAssignedTo(String assignedTo) {
        return opportunityRepository.countByAssignedTo(assignedTo);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByStage(Opportunity.OpportunityStage stage) {
        return opportunityRepository.getTotalAmountByStage(stage);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalWeightedAmountByStage(Opportunity.OpportunityStage stage) {
        return opportunityRepository.getTotalWeightedAmountByStage(stage);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAverageAmount() {
        return opportunityRepository.getAverageAmount();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalWonAmount() {
        return opportunityRepository.getTotalWonAmount();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalLostAmount() {
        return opportunityRepository.getTotalLostAmount();
    }

    @Override
    @Transactional(readOnly = true)
    public double getWinRateByAssignedTo(String assignedTo) {
        long totalOpportunities = opportunityRepository.getTotalOpportunitiesByAssignedTo(assignedTo);
        long wonOpportunities = opportunityRepository.getWonOpportunitiesByAssignedTo(assignedTo);
        
        if (totalOpportunities == 0) {
            return 0.0;
        }
        
        return (double) wonOpportunities / totalOpportunities * 100;
    }

    // Additional methods for filtering and analytics
    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getOpportunitiesByCustomerId(Long customerId, Pageable pageable) {
        return opportunityRepository.findByCustomerId(customerId, pageable)
                .map(OpportunityResponse::fromOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByLeadId(Long leadId) {
        return opportunityRepository.findByLeadId(leadId)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesBySource(String source) {
        return opportunityRepository.findBySource(source)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByCampaignId(String campaignId) {
        return opportunityRepository.findByCampaignId(campaignId)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesWithNoRecentActivity(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return opportunityRepository.findOpportunitiesWithNoRecentActivity(cutoffDate)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesNeedingFollowUp() {
        return opportunityRepository.findOpportunitiesNeedingFollowUp(LocalDateTime.now())
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByExpectedCloseDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return opportunityRepository.findByExpectedCloseDateBetween(startDate, endDate)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByActualCloseDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return opportunityRepository.findByActualCloseDateBetween(startDate, endDate)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return opportunityRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByProbabilityRange(Integer minProbability, Integer maxProbability) {
        return opportunityRepository.findByProbabilityBetween(minProbability, maxProbability)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return opportunityRepository.findByAmountBetween(minAmount, maxAmount)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByCustomerEmail(String customerEmail) {
        return opportunityRepository.findByCustomerEmail(customerEmail)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityResponse> getOpportunitiesByNextAction(String nextAction) {
        return opportunityRepository.findByNextAction(nextAction)
                .stream()
                .map(OpportunityResponse::fromOpportunity)
                .toList();
    }
} 