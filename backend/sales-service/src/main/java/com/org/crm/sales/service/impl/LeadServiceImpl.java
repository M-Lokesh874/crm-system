package com.org.crm.sales.service.impl;

import com.org.crm.common.events.BaseEvent;
import com.org.crm.common.events.LeadEvents;
import com.org.crm.common.events.EventPublisher;
import com.org.crm.sales.exception.GlobalExceptionHandler;
import com.org.crm.sales.model.Lead;
import com.org.crm.sales.repository.LeadRepository;
import com.org.crm.sales.service.LeadService;
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
 * Implementation of LeadService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final EventPublisher eventPublisher;

    @Override
    public LeadResponse createLead(CreateLeadRequest request) {
        log.info("Creating new lead with email: {}", request.email());

        // Check if email already exists
        if (leadRepository.existsByEmail(request.email())) {
            throw new GlobalExceptionHandler.LeadAlreadyExistsException("Lead with email " + request.email() + " already exists");
        }

        // Create lead entity
        Lead lead = Lead.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .company(request.company())
                .jobTitle(request.jobTitle())
                .website(request.website())
                .industry(request.industry())
                .companySize(request.companySize())
                .annualRevenue(request.annualRevenue())
                .description(request.description())
                .notes(request.notes())
                .status(request.status() != null ? request.status() : Lead.LeadStatus.NEW)
                .source(request.source())
                .priority(request.priority() != null ? request.priority() : Lead.LeadPriority.MEDIUM)
                .assignedTo(request.assignedTo())
                .expectedValue(request.expectedValue())
                .closeDate(request.closeDate())
                .build();

        // Save lead
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead created successfully with ID: {}", savedLead.getId());

        // Publish lead created event
        BaseEvent event = new LeadEvents.LeadCreatedEvent(
                savedLead.getId(),
                savedLead.getEmail(),
                savedLead.getFirstName(),
                savedLead.getLastName(),
                savedLead.getCompany(),
                savedLead.getIndustry(),
                savedLead.getAssignedTo(),
                savedLead.getExpectedValue(),
                savedLead.getStatus().toString()
        );
        eventPublisher.publishLeadEvent(event);

        return LeadResponse.fromLead(savedLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadResponse> getLeadById(Long id) {
        log.debug("Fetching lead by ID: {}", id);
        return leadRepository.findById(id)
                .map(LeadResponse::fromLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadResponse> getLeadByEmail(String email) {
        log.debug("Fetching lead by email: {}", email);
        return leadRepository.findByEmail(email)
                .map(LeadResponse::fromLead);
    }

    @Override
    public LeadResponse updateLead(Long id, UpdateLeadRequest request) {
        log.info("Updating lead with ID: {}", id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        // Update lead fields
        if (request.firstName() != null) lead.setFirstName(request.firstName());
        if (request.lastName() != null) lead.setLastName(request.lastName());
        if (request.phone() != null) lead.setPhone(request.phone());
        if (request.company() != null) lead.setCompany(request.company());
        if (request.jobTitle() != null) lead.setJobTitle(request.jobTitle());
        if (request.website() != null) lead.setWebsite(request.website());
        if (request.industry() != null) lead.setIndustry(request.industry());
        if (request.companySize() != null) lead.setCompanySize(request.companySize());
        if (request.annualRevenue() != null) lead.setAnnualRevenue(request.annualRevenue());
        if (request.description() != null) lead.setDescription(request.description());
        if (request.notes() != null) lead.setNotes(request.notes());
        if (request.status() != null) lead.setStatus(request.status());
        if (request.source() != null) lead.setSource(request.source());
        if (request.priority() != null) lead.setPriority(request.priority());
        if (request.assignedTo() != null) lead.setAssignedTo(request.assignedTo());
        if (request.expectedValue() != null) lead.setExpectedValue(request.expectedValue());
        if (request.closeDate() != null) lead.setCloseDate(request.closeDate());

        Lead updatedLead = leadRepository.save(lead);
        log.info("Lead updated successfully with ID: {}", updatedLead.getId());

        // Publish lead updated event
        BaseEvent event = new LeadEvents.LeadUpdatedEvent(
                updatedLead.getId(),
                updatedLead.getEmail(),
                updatedLead.getFirstName(),
                updatedLead.getLastName(),
                updatedLead.getCompany(),
                updatedLead.getIndustry(),
                updatedLead.getAssignedTo(),
                updatedLead.getExpectedValue(),
                updatedLead.getStatus().toString()
        );
        eventPublisher.publishLeadEvent(event);

        return LeadResponse.fromLead(updatedLead);
    }

    @Override
    public void deleteLead(Long id) {
        log.info("Deleting lead with ID: {}", id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        // Publish lead deleted event before deletion
        BaseEvent event = new LeadEvents.LeadDeletedEvent(
                lead.getId(),
                lead.getEmail()
        );
        eventPublisher.publishLeadEvent(event);

        leadRepository.deleteById(id);
        log.info("Lead deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadResponse> getAllLeads(Pageable pageable) {
        log.debug("Fetching all leads with pagination: {}", pageable);
        return leadRepository.findAll(pageable)
                .map(LeadResponse::fromLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadResponse> searchLeads(String searchTerm, Pageable pageable) {
        log.debug("Searching leads with term: {}", searchTerm);
        return leadRepository.searchLeads(searchTerm, pageable)
                .map(LeadResponse::fromLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadResponse> getLeadsByStatus(Lead.LeadStatus status, Pageable pageable) {
        log.debug("Fetching leads by status: {}", status);
        return leadRepository.findByStatus(status, pageable)
                .map(LeadResponse::fromLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadResponse> getLeadsByAssignedTo(String assignedTo, Pageable pageable) {
        log.debug("Fetching leads assigned to: {}", assignedTo);
        return leadRepository.findByAssignedTo(assignedTo, pageable)
                .map(LeadResponse::fromLead);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsBySource(Lead.LeadSource source) {
        log.debug("Fetching leads by source: {}", source);
        return leadRepository.findBySource(source)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByPriority(Lead.LeadPriority priority) {
        log.debug("Fetching leads by priority: {}", priority);
        return leadRepository.findByPriority(priority)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getActiveLeads() {
        log.debug("Fetching active leads");
        return leadRepository.findActiveLeads()
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getConvertedLeads() {
        log.debug("Fetching converted leads");
        return leadRepository.findConvertedLeads()
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsWithNoRecentContact(int days) {
        log.debug("Fetching leads with no contact in last {} days", days);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return leadRepository.findLeadsWithNoRecentContact(cutoffDate)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsNeedingFollowUp() {
        log.debug("Fetching leads needing follow-up");
        LocalDateTime followUpDate = LocalDateTime.now().minusDays(7);
        return leadRepository.findLeadsNeedingFollowUp(followUpDate)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    public LeadResponse updateLeadStatus(Long id, Lead.LeadStatus status) {
        log.info("Updating lead status to {} for lead ID: {}", status, id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        lead.setStatus(status);
        Lead updatedLead = leadRepository.save(lead);

        // Publish lead updated event
        BaseEvent event = new LeadEvents.LeadUpdatedEvent(
                updatedLead.getId(),
                updatedLead.getEmail(),
                updatedLead.getFirstName(),
                updatedLead.getLastName(),
                updatedLead.getCompany(),
                updatedLead.getIndustry(),
                updatedLead.getAssignedTo(),
                updatedLead.getExpectedValue(),
                updatedLead.getStatus().toString()
        );
        eventPublisher.publishLeadEvent(event);

        return LeadResponse.fromLead(updatedLead);
    }

    @Override
    public LeadResponse updateLeadAssignment(Long id, String assignedTo) {
        log.info("Updating lead assignment to {} for lead ID: {}", assignedTo, id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        lead.setAssignedTo(assignedTo);
        Lead updatedLead = leadRepository.save(lead);

        // Publish lead updated event
        BaseEvent event = new LeadEvents.LeadUpdatedEvent(
                updatedLead.getId(),
                updatedLead.getEmail(),
                updatedLead.getFirstName(),
                updatedLead.getLastName(),
                updatedLead.getCompany(),
                updatedLead.getIndustry(),
                updatedLead.getAssignedTo(),
                updatedLead.getExpectedValue(),
                updatedLead.getStatus().toString()
        );
        eventPublisher.publishLeadEvent(event);

        return LeadResponse.fromLead(updatedLead);
    }

    @Override
    public LeadResponse updateLeadPriority(Long id, Lead.LeadPriority priority) {
        log.info("Updating lead priority to {} for lead ID: {}", priority, id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        lead.setPriority(priority);
        Lead updatedLead = leadRepository.save(lead);

        // Publish lead updated event
        BaseEvent event = new LeadEvents.LeadUpdatedEvent(
                updatedLead.getId(),
                updatedLead.getEmail(),
                updatedLead.getFirstName(),
                updatedLead.getLastName(),
                updatedLead.getCompany(),
                updatedLead.getIndustry(),
                updatedLead.getAssignedTo(),
                updatedLead.getExpectedValue(),
                updatedLead.getStatus().toString()
        );
        eventPublisher.publishLeadEvent(event);

        return LeadResponse.fromLead(updatedLead);
    }

    @Override
    public LeadResponse updateLastContactDate(Long id) {
        log.info("Updating last contact date for lead ID: {}", id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        lead.setLastContactDate(LocalDateTime.now());
        Lead updatedLead = leadRepository.save(lead);

        return LeadResponse.fromLead(updatedLead);
    }

    @Override
    public LeadResponse convertLeadToCustomer(Long id, Long customerId, Long opportunityId) {
        log.info("Converting lead to customer with ID: {}, customerId: {}, opportunityId: {}", id, customerId, opportunityId);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.LeadNotFoundException("Lead not found with ID: " + id));

        lead.setStatus(Lead.LeadStatus.CONVERTED);
        lead.setConvertedAt(LocalDateTime.now());
        lead.setConvertedToCustomerId(customerId);
        lead.setConvertedToOpportunityId(opportunityId);

        Lead convertedLead = leadRepository.save(lead);

        // Publish lead converted event
        BaseEvent event = new LeadEvents.LeadConvertedEvent(
                convertedLead.getId(),
                convertedLead.getEmail(),
                customerId,
                opportunityId
        );
        eventPublisher.publishLeadEvent(event);

        return LeadResponse.fromLead(convertedLead);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadStatistics getLeadStatistics() {
        log.debug("Fetching lead statistics");

        long totalLeads = leadRepository.count();
        long newLeads = leadRepository.countByStatus(Lead.LeadStatus.NEW);
        long contactedLeads = leadRepository.countByStatus(Lead.LeadStatus.CONTACTED);
        long qualifiedLeads = leadRepository.countByStatus(Lead.LeadStatus.QUALIFIED);
        long proposalSentLeads = leadRepository.countByStatus(Lead.LeadStatus.PROPOSAL_SENT);
        long negotiationLeads = leadRepository.countByStatus(Lead.LeadStatus.NEGOTIATION);
        long closedWonLeads = leadRepository.countByStatus(Lead.LeadStatus.CLOSED_WON);
        long closedLostLeads = leadRepository.countByStatus(Lead.LeadStatus.CLOSED_LOST);
        long convertedLeads = leadRepository.countByStatus(Lead.LeadStatus.CONVERTED);

        long leadsByWebsite = leadRepository.countBySource(Lead.LeadSource.WEBSITE);
        long leadsByReferral = leadRepository.countBySource(Lead.LeadSource.REFERRAL);
        long leadsBySocialMedia = leadRepository.countBySource(Lead.LeadSource.SOCIAL_MEDIA);
        long leadsByColdCall = leadRepository.countBySource(Lead.LeadSource.COLD_CALL);
        long leadsByTradeShow = leadRepository.countBySource(Lead.LeadSource.TRADE_SHOW);
        long leadsByEmailCampaign = leadRepository.countBySource(Lead.LeadSource.EMAIL_CAMPAIGN);
        long leadsByAdvertising = leadRepository.countBySource(Lead.LeadSource.ADVERTISING);
        long leadsByOther = leadRepository.countBySource(Lead.LeadSource.OTHER);

        long lowPriorityLeads = leadRepository.countByPriority(Lead.LeadPriority.LOW);
        long mediumPriorityLeads = leadRepository.countByPriority(Lead.LeadPriority.MEDIUM);
        long highPriorityLeads = leadRepository.countByPriority(Lead.LeadPriority.HIGH);
        long urgentPriorityLeads = leadRepository.countByPriority(Lead.LeadPriority.URGENT);

        BigDecimal totalExpectedValue = leadRepository.getTotalExpectedValueByStatus(null);
        BigDecimal averageExpectedValue = leadRepository.getAverageExpectedValue();

        return new LeadStatistics(
                totalLeads,
                newLeads,
                contactedLeads,
                qualifiedLeads,
                proposalSentLeads,
                negotiationLeads,
                closedWonLeads,
                closedLostLeads,
                convertedLeads,
                leadsByWebsite,
                leadsByReferral,
                leadsBySocialMedia,
                leadsByColdCall,
                leadsByTradeShow,
                leadsByEmailCampaign,
                leadsByAdvertising,
                leadsByOther,
                lowPriorityLeads,
                mediumPriorityLeads,
                highPriorityLeads,
                urgentPriorityLeads,
                totalExpectedValue != null ? totalExpectedValue : BigDecimal.ZERO,
                averageExpectedValue != null ? averageExpectedValue : BigDecimal.ZERO
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadResponse> getTopLeadsByExpectedValue(Pageable pageable) {
        log.debug("Fetching top leads by expected value");
        return leadRepository.findTopLeadsByExpectedValue(pageable)
                .map(LeadResponse::fromLead);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return leadRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByStatus(Lead.LeadStatus status) {
        return leadRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountBySource(Lead.LeadSource source) {
        return leadRepository.countBySource(source);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByPriority(Lead.LeadPriority priority) {
        return leadRepository.countByPriority(priority);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalExpectedValueByStatus(Lead.LeadStatus status) {
        return leadRepository.getTotalExpectedValueByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAverageExpectedValue() {
        return leadRepository.getAverageExpectedValue();
    }

    @Override
    @Transactional(readOnly = true)
    public double getConversionRateBySource(Lead.LeadSource source) {
        long totalLeads = leadRepository.countBySource(source);
        long convertedLeads = leadRepository.getConvertedLeadsBySource(source);
        
        if (totalLeads == 0) {
            return 0.0;
        }
        
        return (double) convertedLeads / totalLeads * 100;
    }

    // Additional methods for filtering and analytics
    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByCompany(String company) {
        return leadRepository.findByCompanyContainingIgnoreCase(company)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByIndustry(String industry) {
        return leadRepository.findByIndustryIgnoreCase(industry)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByCloseDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return leadRepository.findByCloseDateBetween(startDate, endDate)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return leadRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByCompanySize(String companySize) {
        return leadRepository.findByCompanySizeIgnoreCase(companySize)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getLeadsByAnnualRevenueRange(BigDecimal minRevenue, BigDecimal maxRevenue) {
        return leadRepository.findByAnnualRevenueBetween(minRevenue, maxRevenue)
                .stream()
                .map(LeadResponse::fromLead)
                .toList();
    }
} 