package com.org.crm.customer.service.impl;

import com.org.crm.common.events.BaseEvent;
import com.org.crm.common.events.CustomerEvents;
import com.org.crm.common.events.EventPublisher;
import com.org.crm.customer.exception.GlobalExceptionHandler;
import com.org.crm.customer.model.Customer;
import com.org.crm.customer.repository.CustomerRepository;
import com.org.crm.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of CustomerService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EventPublisher eventPublisher;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer with email: {}", request.email());

        // Check if email already exists
        if (customerRepository.existsByEmail(request.email())) {
            throw new GlobalExceptionHandler.CustomerAlreadyExistsException("Customer with email " + request.email() + " already exists");
        }

        // Create customer entity
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .company(request.company())
                .jobTitle(request.jobTitle())
                .address(request.address())
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .postalCode(request.postalCode())
                .website(request.website())
                .notes(request.notes())
                .status(request.status() != null ? request.status() : Customer.CustomerStatus.ACTIVE)
                .source(request.source())
                .assignedTo(request.assignedTo())
                .build();

        // Save customer
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());

        // Publish customer created event
        BaseEvent event = new CustomerEvents.CustomerCreatedEvent(
                savedCustomer.getId(),
                savedCustomer.getEmail(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getCompany(),
                savedCustomer.getIndustry(),
                savedCustomer.getAssignedTo()
        );
        eventPublisher.publishCustomerEvent(event);

        return CustomerResponse.fromCustomer(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerResponse> getCustomerById(Long id) {
        log.debug("Fetching customer by ID: {}", id);
        return customerRepository.findById(id)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerResponse> getCustomerByEmail(String email) {
        log.debug("Fetching customer by email: {}", email);
        return customerRepository.findByEmail(email)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomerNotFoundException("Customer not found with ID: " + id));

        // Update customer fields
        if (request.firstName() != null) customer.setFirstName(request.firstName());
        if (request.lastName() != null) customer.setLastName(request.lastName());
        if (request.phone() != null) customer.setPhone(request.phone());
        if (request.company() != null) customer.setCompany(request.company());
        if (request.jobTitle() != null) customer.setJobTitle(request.jobTitle());
        if (request.address() != null) customer.setAddress(request.address());
        if (request.city() != null) customer.setCity(request.city());
        if (request.state() != null) customer.setState(request.state());
        if (request.country() != null) customer.setCountry(request.country());
        if (request.postalCode() != null) customer.setPostalCode(request.postalCode());
        if (request.website() != null) customer.setWebsite(request.website());
        if (request.notes() != null) customer.setNotes(request.notes());
        if (request.status() != null) customer.setStatus(request.status());
        if (request.source() != null) customer.setSource(request.source());
        if (request.assignedTo() != null) customer.setAssignedTo(request.assignedTo());

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getId());

        // Publish customer updated event
        BaseEvent event = new CustomerEvents.CustomerUpdatedEvent(
                updatedCustomer.getId(),
                updatedCustomer.getEmail(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getCompany(),
                updatedCustomer.getIndustry(),
                updatedCustomer.getAssignedTo()
        );
        eventPublisher.publishCustomerEvent(event);

        return CustomerResponse.fromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomerNotFoundException("Customer not found with ID: " + id));

        // Publish customer deleted event before deletion
        BaseEvent event = new CustomerEvents.CustomerDeletedEvent(
                customer.getId(),
                customer.getEmail()
        );
        eventPublisher.publishCustomerEvent(event);

        customerRepository.deleteById(id);
        log.info("Customer deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        log.debug("Fetching all customers with pagination: {}", pageable);
        return customerRepository.findAll(pageable)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomers(String searchTerm, Pageable pageable) {
        log.debug("Searching customers with term: {}", searchTerm);
        return customerRepository.searchCustomers(searchTerm, pageable)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomersByStatus(Customer.CustomerStatus status, Pageable pageable) {
        log.debug("Fetching customers by status: {}", status);
        return customerRepository.findByStatus(status, pageable)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomersByAssignedTo(String assignedTo, Pageable pageable) {
        log.debug("Fetching customers assigned to: {}", assignedTo);
        return customerRepository.findByAssignedTo(assignedTo, pageable)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByCompany(String company) {
        log.debug("Fetching customers by company: {}", company);
        return customerRepository.findByCompanyContainingIgnoreCase(company)
                .stream()
                .map(CustomerResponse::fromCustomer)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersBySource(Customer.CustomerSource source) {
        log.debug("Fetching customers by source: {}", source);
        return customerRepository.findBySource(source)
                .stream()
                .map(CustomerResponse::fromCustomer)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersWithNoRecentContact(int days) {
        log.debug("Fetching customers with no contact in last {} days", days);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return customerRepository.findCustomersWithNoRecentContact(cutoffDate)
                .stream()
                .map(CustomerResponse::fromCustomer)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomerStatus(Long id, Customer.CustomerStatus status) {
        log.info("Updating customer status to {} for customer ID: {}", status, id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomerNotFoundException("Customer not found with ID: " + id));

        customer.setStatus(status);
        Customer updatedCustomer = customerRepository.save(customer);

        // Publish customer updated event
        BaseEvent event = new CustomerEvents.CustomerUpdatedEvent(
                updatedCustomer.getId(),
                updatedCustomer.getEmail(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getCompany(),
                updatedCustomer.getIndustry(),
                updatedCustomer.getAssignedTo()
        );
        eventPublisher.publishCustomerEvent(event);

        return CustomerResponse.fromCustomer(updatedCustomer);
    }

    @Override
    public CustomerResponse updateCustomerAssignment(Long id, String assignedTo) {
        log.info("Updating customer assignment to {} for customer ID: {}", assignedTo, id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomerNotFoundException("Customer not found with ID: " + id));

        customer.setAssignedTo(assignedTo);
        Customer updatedCustomer = customerRepository.save(customer);

        // Publish customer updated event
        BaseEvent event = new CustomerEvents.CustomerUpdatedEvent(
                updatedCustomer.getId(),
                updatedCustomer.getEmail(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getCompany(),
                updatedCustomer.getIndustry(),
                updatedCustomer.getAssignedTo()
        );
        eventPublisher.publishCustomerEvent(event);

        return CustomerResponse.fromCustomer(updatedCustomer);
    }

    @Override
    public CustomerResponse updateLastContactDate(Long id) {
        log.info("Updating last contact date for customer ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomerNotFoundException("Customer not found with ID: " + id));

        customer.setLastContactDate(LocalDateTime.now());
        Customer updatedCustomer = customerRepository.save(customer);

        return CustomerResponse.fromCustomer(updatedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerStatistics getCustomerStatistics() {
        log.debug("Fetching customer statistics");

        long totalCustomers = customerRepository.count();
        long activeCustomers = customerRepository.countByStatus(Customer.CustomerStatus.ACTIVE);
        long inactiveCustomers = customerRepository.countByStatus(Customer.CustomerStatus.INACTIVE);
        long prospectCustomers = customerRepository.countByStatus(Customer.CustomerStatus.PROSPECT);
        long leadCustomers = customerRepository.countByStatus(Customer.CustomerStatus.LEAD);
        long vipCustomers = customerRepository.countByStatus(Customer.CustomerStatus.VIP);

        long customersByWebsite = customerRepository.countBySource(Customer.CustomerSource.WEBSITE);
        long customersByReferral = customerRepository.countBySource(Customer.CustomerSource.REFERRAL);
        long customersBySocialMedia = customerRepository.countBySource(Customer.CustomerSource.SOCIAL_MEDIA);
        long customersByColdCall = customerRepository.countBySource(Customer.CustomerSource.COLD_CALL);
        long customersByTradeShow = customerRepository.countBySource(Customer.CustomerSource.TRADE_SHOW);
        long customersByOther = customerRepository.countBySource(Customer.CustomerSource.OTHER);

        return new CustomerStatistics(
                totalCustomers,
                activeCustomers,
                inactiveCustomers,
                prospectCustomers,
                leadCustomers,
                vipCustomers,
                customersByWebsite,
                customersByReferral,
                customersBySocialMedia,
                customersByColdCall,
                customersByTradeShow,
                customersByOther
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getTopCustomersByRevenue(Pageable pageable) {
        log.debug("Fetching top customers by revenue");
        return customerRepository.findTopCustomersByRevenue(pageable)
                .map(CustomerResponse::fromCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByCity(String city) {
        log.debug("Fetching customers by city: {}", city);
        return customerRepository.findByCityIgnoreCase(city)
                .stream()
                .map(CustomerResponse::fromCustomer)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByCountry(String country) {
        log.debug("Fetching customers by country: {}", country);
        return customerRepository.findByCountryIgnoreCase(country)
                .stream()
                .map(CustomerResponse::fromCustomer)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching customers created between {} and {}", startDate, endDate);
        return customerRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(CustomerResponse::fromCustomer)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerCountByStatus(Customer.CustomerStatus status) {
        return customerRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerCountBySource(Customer.CustomerSource source) {
        return customerRepository.countBySource(source);
    }
} 