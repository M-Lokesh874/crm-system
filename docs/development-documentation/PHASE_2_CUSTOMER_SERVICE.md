# Phase 2: Customer Service Implementation

## Overview

Phase 2 focuses on implementing the Customer Service, which is the core service for managing customer data in the CRM system. This service provides comprehensive customer management capabilities including CRUD operations, search, filtering, and statistics.

## Implementation Details

### 1. Service Architecture

**Service Name:** Customer Service  
**Port:** 8081  
**Database:** PostgreSQL (crm_customer_db)  
**Cache:** Redis  
**Message Broker:** RabbitMQ  

### 2. Core Components

#### 2.1 Customer Entity (`Customer.java`)
```java
@Entity
@Table(name = "customers")
public class Customer {
    // Core fields
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String jobTitle;
    
    // Address fields
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    
    // Business fields
    private String website;
    private String notes;
    private CustomerStatus status;
    private CustomerSource source;
    private String assignedTo;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastContactDate;
    
    // Revenue tracking
    private Integer totalOrders;
    private Double totalRevenue;
}
```

**Enums:**
- `CustomerStatus`: ACTIVE, INACTIVE, PROSPECT, LEAD, CUSTOMER, VIP
- `CustomerSource`: WEBSITE, REFERRAL, SOCIAL_MEDIA, COLD_CALL, TRADE_SHOW, OTHER

#### 2.2 Repository Layer (`CustomerRepository.java`)
```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Basic queries
    Optional<Customer> findByEmail(String email);
    List<Customer> findByStatus(CustomerStatus status);
    List<Customer> findByAssignedTo(String assignedTo);
    
    // Search functionality
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.company) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> searchCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Statistics queries
    long countByStatus(CustomerStatus status);
    long countBySource(CustomerSource source);
    
    // Advanced queries
    @Query("SELECT c FROM Customer c WHERE c.lastContactDate IS NULL OR c.lastContactDate < :date")
    List<Customer> findCustomersWithNoRecentContact(@Param("date") LocalDateTime date);
}
```

#### 2.3 Service Layer (`CustomerService.java` & `CustomerServiceImpl.java`)

**Key Features:**
- CRUD operations with validation
- Search and filtering capabilities
- Status and assignment management
- Contact tracking
- Statistics generation
- Event publishing

**Event Publishing:**
```java
// Customer Created Event
BaseEvent event = new CustomerEvents.CustomerCreatedEvent(
    "customer.created",
    "customer-service",
    savedCustomer.getId().toString(),
    savedCustomer.getEmail(),
    savedCustomer.getFullName()
);
eventPublisher.publishCustomerEvent(event);
```

#### 2.4 Controller Layer (`CustomerController.java`)

**REST Endpoints:**

| Method | Endpoint | Description | Authorization |
|--------|----------|-------------|---------------|
| POST | `/api/v1/customers` | Create customer | ADMIN, SALES_MANAGER, SALES_REP |
| GET | `/api/v1/customers/{id}` | Get customer by ID | ADMIN, SALES_MANAGER, SALES_REP |
| GET | `/api/v1/customers/email/{email}` | Get customer by email | ADMIN, SALES_MANAGER, SALES_REP |
| PUT | `/api/v1/customers/{id}` | Update customer | ADMIN, SALES_MANAGER, SALES_REP |
| DELETE | `/api/v1/customers/{id}` | Delete customer | ADMIN, SALES_MANAGER |
| GET | `/api/v1/customers` | Get all customers (paginated) | ADMIN, SALES_MANAGER, SALES_REP |
| GET | `/api/v1/customers/search` | Search customers | ADMIN, SALES_MANAGER, SALES_REP |
| GET | `/api/v1/customers/status/{status}` | Get customers by status | ADMIN, SALES_MANAGER, SALES_REP |
| GET | `/api/v1/customers/assigned/{assignedTo}` | Get customers by assigned user | ADMIN, SALES_MANAGER, SALES_REP |
| PATCH | `/api/v1/customers/{id}/status` | Update customer status | ADMIN, SALES_MANAGER, SALES_REP |
| PATCH | `/api/v1/customers/{id}/assignment` | Update customer assignment | ADMIN, SALES_MANAGER |
| PATCH | `/api/v1/customers/{id}/contact` | Update last contact date | ADMIN, SALES_MANAGER, SALES_REP |
| GET | `/api/v1/customers/statistics` | Get customer statistics | ADMIN, SALES_MANAGER |
| GET | `/api/v1/customers/top-revenue` | Get top customers by revenue | ADMIN, SALES_MANAGER |

### 3. Security Implementation

#### 3.1 JWT Authentication (`JwtAuthenticationFilter.java`)
- Extracts JWT tokens from Authorization header
- Validates tokens using JwtUtil
- Sets Spring Security context with user details and roles

#### 3.2 Security Configuration (`SecurityConfig.java`)
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // Public endpoints: actuator, swagger-ui, health checks
    // Protected endpoints: all /api/v1/customers/** endpoints
    // Role-based access control using @PreAuthorize
}
```

### 4. Event-Driven Architecture

#### 4.1 Event Types
- `CustomerCreatedEvent`: Published when a new customer is created
- `CustomerUpdatedEvent`: Published when customer information is updated
- `CustomerDeletedEvent`: Published when a customer is deleted

#### 4.2 Event Structure
```java
public static class CustomerCreatedEvent extends BaseEvent {
    private String customerId;
    private String customerEmail;
    private String customerName;
    
    public CustomerCreatedEvent(String eventType, String source, 
                              String customerId, String customerEmail, String customerName) {
        super(eventType, source);
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
    }
}
```

### 5. Configuration

#### 5.1 Application Configuration (`application.yml`)
```yaml
server:
  port: 8081

spring:
  application:
    name: customer-service
  
  # Database Configuration
  datasource:
    url: jdbc:postgresql://localhost:5432/crm_customer_db
    username: crm_user
    password: crm_password
    
  # JPA Configuration
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    
  # RabbitMQ Configuration
  rabbitmq:
    host: localhost
    port: 5672
    username: crm_user
    password: crm_password
    virtual-host: /crm
    
  # Redis Configuration
  redis:
    host: localhost
    port: 6379
    password: crm_password
    database: 1

# Eureka Client Configuration
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

# JWT Configuration
jwt:
  secret: crmCustomerServiceSecretKeyForJWTTokenGeneration2024
  expiration: 86400000
```

#### 5.2 Docker Configuration (`Dockerfile`)
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/customer-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### 6. Error Handling

#### 6.1 Custom Exceptions
- `CustomerNotFoundException`: When customer is not found
- `CustomerAlreadyExistsException`: When customer with email already exists

#### 6.2 Global Exception Handler (`GlobalExceptionHandler.java`)
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handles validation errors, runtime exceptions, custom exceptions
    // Returns structured error responses with appropriate HTTP status codes
}
```

### 7. API Documentation

#### 7.1 Swagger Configuration (`SwaggerConfig.java`)
- OpenAPI 3.0 specification
- Interactive API documentation
- Server configurations for local and production environments

#### 7.2 API Endpoints Documentation
All endpoints are documented with:
- Operation summaries and descriptions
- Parameter descriptions
- Authorization requirements
- Request/response examples

### 8. Monitoring and Health Checks

#### 8.1 Actuator Endpoints
- `/actuator/health`: Service health status
- `/actuator/info`: Service information
- `/actuator/metrics`: Performance metrics
- `/actuator/prometheus`: Prometheus metrics

#### 8.2 Logging Configuration
```yaml
logging:
  level:
    com.org.crm.customer: DEBUG
    org.springframework.security: DEBUG
    org.springframework.web: DEBUG
  file:
    name: logs/customer-service.log
    max-size: 10MB
    max-history: 30
```

### 9. Testing Strategy

#### 9.1 Unit Tests
- Service layer testing with mocked dependencies
- Repository layer testing with test database
- Controller layer testing with MockMvc

#### 9.2 Integration Tests
- End-to-end API testing
- Database integration testing
- Event publishing testing

#### 9.3 Test Coverage Goals
- Service layer: 90%+
- Controller layer: 85%+
- Repository layer: 80%+

### 10. Performance Considerations

#### 10.1 Caching Strategy
- Redis-based caching for frequently accessed data
- Customer lookup by email caching
- Statistics caching with TTL

#### 10.2 Database Optimization
- Indexes on frequently queried fields (email, status, assignedTo)
- Pagination for large result sets
- Batch operations for bulk updates

#### 10.3 JVM Optimization
- G1GC garbage collector
- Container-aware memory settings
- Health check integration

### 11. Deployment

#### 11.1 Docker Compose Integration
```yaml
customer-service:
  build:
    context: ./backend/customer-service
  ports:
    - "8081:8081"
  depends_on:
    discovery-server:
      condition: service_healthy
    postgres:
      condition: service_healthy
    redis:
      condition: service_healthy
    rabbitmq:
      condition: service_healthy
  environment:
    SPRING_APPLICATION_NAME: customer-service
    EUREKA_SERVER_SERVICE_URL_DEFAULT_ZONE: http://discovery-server:8761/eureka/
```

#### 11.2 Service Discovery
- Registers with Eureka Server
- Provides health check endpoint
- Supports load balancing

### 12. Next Steps

The Customer Service implementation provides a solid foundation for the CRM system. The next phases will focus on:

1. **Sales Service**: Lead management, opportunity tracking, sales pipeline
2. **Task Service**: Task management, assignment, tracking
3. **Notification Service**: Event-driven notifications, email/SMS integration
4. **API Gateway**: Centralized routing, authentication, rate limiting
5. **Frontend Development**: React-based user interface

### 13. Success Metrics

- **Functionality**: All CRUD operations working correctly
- **Performance**: Response times under 200ms for basic operations
- **Reliability**: 99.9% uptime with proper error handling
- **Security**: JWT authentication and role-based access control working
- **Monitoring**: Health checks and metrics properly configured
- **Documentation**: Complete API documentation with Swagger UI 