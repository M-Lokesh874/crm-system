# Phase 1: Project Setup & Architecture

## Overview
This phase focuses on setting up the project structure, defining the architecture, and creating the foundational components for the CRM system.

## Step 1: Project Structure Creation

### 1.1 Create Directory Structure
```bash
crm-system/
├── backend/
│   ├── customer-service/
│   ├── sales-service/
│   ├── task-service/
│   ├── notification-service/
│   ├── crm-gateway/
│   ├── discovery-server/
│   └── crm-common/
├── frontend/
│   └── crm-react-app/
├── docs/
│   ├── development-documentation/
│   ├── ai-prompt-library/
│   └── reflection-report/
├── docker-compose.yml
└── README.md
```

### 1.2 Backend Services Architecture

#### Customer Service (`customer-service`)
- **Port**: 8081
- **Database**: PostgreSQL (crm_customers)
- **Responsibilities**:
  - Customer CRUD operations
  - Contact history management
  - Customer search and filtering
  - Customer analytics

#### Sales Service (`sales-service`)
- **Port**: 8082
- **Database**: PostgreSQL (crm_sales)
- **Responsibilities**:
  - Lead management
  - Sales pipeline tracking
  - Deal management
  - Sales reporting

#### Task Service (`task-service`)
- **Port**: 8083
- **Database**: PostgreSQL (crm_tasks)
- **Responsibilities**:
  - Task creation and assignment
  - Follow-up scheduling
  - Reminder management
  - Task status tracking

#### Notification Service (`notification-service`)
- **Port**: 8084
- **Database**: PostgreSQL (crm_notifications)
- **Responsibilities**:
  - Follow-up reminders
  - Lead assignment notifications
  - Deal closure notifications
  - Task deadline alerts

#### CRM Gateway (`crm-gateway`)
- **Port**: 8080
- **Responsibilities**:
  - Unified CRM API
  - Authentication and authorization
  - Rate limiting
  - Request routing

#### Discovery Server (`discovery-server`)
- **Port**: 8761
- **Responsibilities**:
  - Service registration
  - Load balancing
  - Health monitoring

#### CRM Common (`crm-common`)
- **Responsibilities**:
  - Shared DTOs
  - Event definitions
  - Security utilities
  - Common configurations

## Step 2: Data Model Design

### 2.1 Customer Entity
```java
@Entity
@Table(name = "customers")
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

### 2.2 Lead Entity
```java
@Entity
@Table(name = "leads")
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal value;
    
    @Enumerated(EnumType.STRING)
    private LeadStage stage;
    
    @Column(name = "assigned_to")
    private Long assignedTo;
    
    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 2.3 Task Entity
```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TaskType type;
    
    @Enumerated(EnumType.STRING)
    private Priority priority;
    
    @Column(name = "assigned_to")
    private Long assignedTo;
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "lead_id")
    private Long leadId;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

## Step 3: Event-Driven Architecture Design

### 3.1 Event Types
1. **Customer Events**:
   - `customer.created`
   - `customer.updated`
   - `customer.deleted`

2. **Lead Events**:
   - `lead.created`
   - `lead.stage.changed`
   - `lead.assigned`
   - `lead.closed`

3. **Task Events**:
   - `task.created`
   - `task.assigned`
   - `task.completed`
   - `task.due.soon`

### 3.2 Event Structure
```java
public abstract class BaseEvent {
    private String eventId = UUID.randomUUID().toString();
    private String eventType;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String source;
    private String version = "1.0";
}
```

## Step 4: API Design

### 4.1 Customer Service APIs
- `POST /api/customers` - Create customer
- `GET /api/customers` - List customers
- `GET /api/customers/{id}` - Get customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer
- `GET /api/customers/search` - Search customers

### 4.2 Sales Service APIs
- `POST /api/leads` - Create lead
- `GET /api/leads` - List leads
- `GET /api/leads/{id}` - Get lead
- `PUT /api/leads/{id}` - Update lead
- `PUT /api/leads/{id}/stage` - Update lead stage
- `DELETE /api/leads/{id}` - Delete lead

### 4.3 Task Service APIs
- `POST /api/tasks` - Create task
- `GET /api/tasks` - List tasks
- `GET /api/tasks/{id}` - Get task
- `PUT /api/tasks/{id}` - Update task
- `PUT /api/tasks/{id}/status` - Update task status
- `DELETE /api/tasks/{id}` - Delete task

## Step 5: Security Design

### 5.1 Authentication
- JWT-based authentication
- Role-based access control
- Token refresh mechanism

### 5.2 Authorization Roles
- **ADMIN**: Full access to all features
- **SALES_MANAGER**: Manage sales pipeline and team
- **SALES_REP**: Manage assigned customers and leads
- **USER**: Basic access to assigned tasks

## Step 6: Database Design

### 6.1 Database Schema
```sql
-- Customers Database
CREATE DATABASE crm_customers;

-- Sales Database
CREATE DATABASE crm_sales;

-- Tasks Database
CREATE DATABASE crm_tasks;

-- Notifications Database
CREATE DATABASE crm_notifications;
```

### 6.2 Connection Configuration
- Each service connects to its dedicated database
- Redis for caching and session management
- RabbitMQ for event messaging

## Step 7: Docker Configuration

### 7.1 Docker Compose Setup
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
```

## Next Steps

1. **Create Maven Parent POM** with dependency management
2. **Set up individual service projects** with Spring Boot
3. **Configure service discovery** with Eureka
4. **Implement basic CRUD operations** for each service
5. **Set up event publishing** and consumption
6. **Create API Gateway** with routing configuration

## Deliverables

- [x] Project structure created
- [x] Architecture design documented
- [x] Data models defined
- [x] API specifications outlined
- [x] Security requirements defined
- [ ] Maven parent POM created
- [ ] Individual service projects set up
- [ ] Docker configuration completed 