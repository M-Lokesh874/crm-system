# Phase 2: Backend Development

## Overview
This phase focuses on implementing the backend microservices for the CRM system, including the common library, individual services, and infrastructure components.

## Step 1: CRM Common Library Implementation

### 1.1 Maven Parent POM
**File**: `pom.xml`
- **Purpose**: Centralized dependency management for all CRM services
- **Key Features**:
  - Spring Boot 3.2.5 and Spring Cloud 2023.0.1
  - PostgreSQL driver and JWT dependencies
  - Swagger/OpenAPI documentation
  - Lombok for code generation

### 1.2 Event-Driven Architecture
**Files Created**:
- `BaseEvent.java` - Base class for all events
- `CustomerEvents.java` - Customer lifecycle events
- `LeadEvents.java` - Sales pipeline events
- `TaskEvents.java` - Task management events

**Event Types Implemented**:
- **Customer Events**: `customer.created`, `customer.updated`, `customer.deleted`
- **Lead Events**: `lead.created`, `lead.stage.changed`, `lead.assigned`, `lead.closed`
- **Task Events**: `task.created`, `task.assigned`, `task.completed`, `task.due.soon`

### 1.3 Data Transfer Objects (DTOs)
**Files Created**:
- `CustomerDTO.java` - Customer request/response DTOs
- `LeadDTO.java` - Lead request/response DTOs
- `TaskDTO.java` - Task request/response DTOs

**Features**:
- Input validation with Bean Validation
- Comprehensive request/response structures
- Search and filtering capabilities
- Pagination support

### 1.4 Security Components
**Files Created**:
- `JwtUtil.java` - JWT token generation and validation
- `JwtAuthenticationFilter.java` - JWT authentication filter

**Security Features**:
- JWT-based authentication
- Role-based access control
- Token validation and expiration
- Secure token generation

### 1.5 RabbitMQ Configuration
**File**: `RabbitMQConfig.java`
- **Queues**: Customer, Lead, and Task event queues
- **Exchange**: Topic exchange for event routing
- **Bindings**: Routing key bindings for event distribution
- **Message Converter**: JSON message conversion

### 1.6 Event Publisher
**File**: `EventPublisher.java`
- **Purpose**: Publish events to RabbitMQ
- **Features**: Error handling and logging
- **Methods**: `publishCustomerEvent()`, `publishLeadEvent()`, `publishTaskEvent()`

## Step 2: Discovery Server Implementation

### 2.1 Maven POM
**File**: `backend/discovery-server/pom.xml`
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```

### 2.2 Application Configuration
**File**: `backend/discovery-server/src/main/resources/application.yml`
```yaml
server:
  port: 8761

spring:
  application:
    name: discovery-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    wait-time-in-ms-when-sync-empty: 0
```

### 2.3 Main Application Class
**File**: `backend/discovery-server/src/main/java/com/org/crm/discovery/DiscoveryServerApplication.java`
```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
```

## Step 3: Customer Service Implementation

### 3.1 Maven POM
**File**: `backend/customer-service/pom.xml`
```xml
<dependencies>
    <dependency>
        <groupId>com.org.crm</groupId>
        <artifactId>crm-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```

### 3.2 Application Configuration
**File**: `backend/customer-service/src/main/resources/application.yml`
```yaml
server:
  port: 8081

spring:
  application:
    name: customer-service
  datasource:
    url: jdbc:postgresql://localhost:5432/crm_customers
    username: crmuser
    password: crmpassword
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### 3.3 Entity Implementation
**File**: `backend/customer-service/src/main/java/com/org/crm/customer/model/Customer.java`
```java
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private String phone;
    
    @Column
    private String company;
    
    @Column
    private String industry;
    
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    
    @Column(name = "assigned_to")
    private Long assignedTo;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 3.4 Repository Implementation
**File**: `backend/customer-service/src/main/java/com/org/crm/customer/repository/CustomerRepository.java`
```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    List<Customer> findByAssignedTo(Long assignedTo);
    List<Customer> findByStatus(CustomerStatus status);
    Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName, Pageable pageable);
}
```

### 3.5 Service Implementation
**File**: `backend/customer-service/src/main/java/com/org/crm/customer/service/CustomerService.java`
```java
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final EventPublisher eventPublisher;
    
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        // Implementation with event publishing
    }
    
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        // Implementation with event publishing
    }
    
    public void deleteCustomer(Long id) {
        // Implementation with event publishing
    }
    
    public PageResponse<CustomerResponse> searchCustomers(CustomerSearchRequest request) {
        // Implementation with pagination
    }
}
```

### 3.6 Controller Implementation
**File**: `backend/customer-service/src/main/java/com/org/crm/customer/controller/CustomerController.java`
```java
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer API", description = "Endpoints for customer management")
public class CustomerController {
    private final CustomerService customerService;
    
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        // Implementation
    }
    
    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> searchCustomers(CustomerSearchRequest request) {
        // Implementation
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        // Implementation
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
        // Implementation
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        // Implementation
    }
}
```

## Step 4: Sales Service Implementation

### 4.1 Similar Structure to Customer Service
- **Port**: 8082
- **Database**: crm_sales
- **Entity**: Lead
- **Repository**: LeadRepository
- **Service**: LeadService
- **Controller**: LeadController

### 4.2 Key Features
- Lead CRUD operations
- Stage management (NEW, CONTACTED, QUALIFIED, PROPOSAL, NEGOTIATION, CLOSED)
- Assignment tracking
- Value and expected close date management
- Event publishing for lead lifecycle

## Step 5: Task Service Implementation

### 5.1 Similar Structure to Customer Service
- **Port**: 8083
- **Database**: crm_tasks
- **Entity**: Task
- **Repository**: TaskRepository
- **Service**: TaskService
- **Controller**: TaskController

### 5.2 Key Features
- Task CRUD operations
- Type management (FOLLOW_UP, MEETING, CALL, EMAIL)
- Priority levels (LOW, MEDIUM, HIGH, URGENT)
- Status tracking (PENDING, IN_PROGRESS, COMPLETED)
- Due date management
- Assignment tracking

## Step 6: Notification Service Implementation

### 6.1 Similar Structure to Other Services
- **Port**: 8084
- **Database**: crm_notifications
- **Entity**: Notification
- **Repository**: NotificationRepository
- **Service**: NotificationService
- **Controller**: NotificationController

### 6.2 Key Features
- Event consumption from RabbitMQ
- Multi-channel notification support (Email, SMS, Push)
- Notification status tracking
- Template-based message generation
- Automated notification triggers

## Step 7: API Gateway Implementation

### 7.1 Maven POM
**File**: `backend/crm-gateway/pom.xml`
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>
```

### 7.2 Application Configuration
**File**: `backend/crm-gateway/src/main/resources/application.yml`
```yaml
server:
  port: 8080

spring:
  application:
    name: crm-gateway
  cloud:
    gateway:
      routes:
        - id: customer-service
          uri: lb://CUSTOMER-SERVICE
          predicates:
            - Path=/api/customers/**
        - id: sales-service
          uri: lb://SALES-SERVICE
          predicates:
            - Path=/api/leads/**
        - id: task-service
          uri: lb://TASK-SERVICE
          predicates:
            - Path=/api/tasks/**
        - id: notification-service
          uri: lb://NOTIFICATION-SERVICE
          predicates:
            - Path=/api/notifications/**
```

### 7.3 Gateway Configuration
**File**: `backend/crm-gateway/src/main/java/com/org/crm/gateway/config/GatewayConfig.java`
```java
@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("customer-service", r -> r.path("/api/customers/**")
                .filters(f -> f
                    .circuitBreaker(config -> config
                        .setName("customerServiceCircuitBreaker")
                        .setFallbackUri("forward:/fallback/customer-service"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver())))
                .uri("lb://CUSTOMER-SERVICE"))
            .build();
    }
}
```

## Step 8: Docker Configuration

### 8.1 Docker Compose
**File**: `docker-compose.yml`
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_USER: crmuser
      POSTGRES_PASSWORD: crmpassword
      POSTGRES_DB: crm_customers
    ports:
      - "5432:5432"
    networks:
      - crm-net

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks:
      - crm-net

  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
    networks:
      - crm-net

  discovery-server:
    build:
      context: ./backend/discovery-server
    ports:
      - "8761:8761"
    networks:
      - crm-net

  customer-service:
    build:
      context: ./backend/customer-service
    ports:
      - "8081:8081"
    depends_on:
      - discovery-server
      - postgres
      - rabbitmq
    networks:
      - crm-net

  sales-service:
    build:
      context: ./backend/sales-service
    ports:
      - "8082:8082"
    depends_on:
      - discovery-server
      - postgres
      - rabbitmq
    networks:
      - crm-net

  task-service:
    build:
      context: ./backend/task-service
    ports:
      - "8083:8083"
    depends_on:
      - discovery-server
      - postgres
      - rabbitmq
    networks:
      - crm-net

  notification-service:
    build:
      context: ./backend/notification-service
    ports:
      - "8084:8084"
    depends_on:
      - discovery-server
      - postgres
      - rabbitmq
    networks:
      - crm-net

  crm-gateway:
    build:
      context: ./backend/crm-gateway
    ports:
      - "8080:8080"
    depends_on:
      - discovery-server
      - redis
    networks:
      - crm-net

networks:
  crm-net:
    driver: bridge
```

## Next Steps

1. **Implement Individual Services** - Complete the implementation of each microservice
2. **Add Comprehensive Testing** - Unit tests, integration tests, and API tests
3. **Implement Security** - JWT authentication and authorization
4. **Add Monitoring** - Health checks, metrics, and logging
5. **Create Frontend** - React application for CRM interface

## Deliverables

- [x] Maven parent POM created
- [x] CRM Common library implemented
- [x] Event-driven architecture established
- [x] DTOs and security components created
- [x] RabbitMQ configuration completed
- [ ] Discovery Server implemented
- [ ] Customer Service implemented
- [ ] Sales Service implemented
- [ ] Task Service implemented
- [ ] Notification Service implemented
- [ ] API Gateway implemented
- [ ] Docker configuration completed 