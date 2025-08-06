# Phase 5: Sales Service Implementation

## Overview
The Sales Service manages leads and opportunities in the CRM system, providing comprehensive sales pipeline management functionality.

## Architecture

### Service Details
- **Port**: 8082
- **Database**: PostgreSQL (crm_sales_db)
- **Discovery**: Eureka Client
- **Security**: JWT-based authentication

### Key Components

#### 1. Lead Entity
```java
@Entity
@Table(name = "leads")
public class Lead {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private LeadStatus status;
    private LeadSource source;
    private LeadPriority priority;
    private String assignedTo;
    private String description;
    // ... other fields
}
```

#### 2. Opportunity Entity
```java
@Entity
@Table(name = "opportunities")
public class Opportunity {
    private Long id;
    private String name;
    private Long customerId;
    private Long leadId;
    private BigDecimal amount;
    private OpportunityStage stage;
    private OpportunityType type;
    private String assignedTo;
    private String description;
    // ... other fields
}
```

#### 3. Sales Endpoints
- `POST /api/v1/leads` - Create new lead
- `GET /api/v1/leads/{id}` - Get lead by ID
- `PUT /api/v1/leads/{id}` - Update lead
- `DELETE /api/v1/leads/{id}` - Delete lead
- `POST /api/v1/opportunities` - Create new opportunity
- `GET /api/v1/opportunities/{id}` - Get opportunity by ID
- `PUT /api/v1/opportunities/{id}` - Update opportunity
- `DELETE /api/v1/opportunities/{id}` - Delete opportunity

## Implementation Details

### Lead Management
```java
@Service
public class LeadService {
    public LeadResponse createLead(CreateLeadRequest request)
    public LeadResponse updateLead(Long id, UpdateLeadRequest request)
    public void deleteLead(Long id)
    public LeadResponse convertLeadToCustomer(Long leadId)
}
```

### Opportunity Management
```java
@Service
public class OpportunityService {
    public OpportunityResponse createOpportunity(CreateOpportunityRequest request)
    public OpportunityResponse updateOpportunity(Long id, UpdateOpportunityRequest request)
    public void deleteOpportunity(Long id)
    public OpportunityResponse updateStage(Long id, OpportunityStage stage)
}
```

### Event Publishing
```java
@Component
public class EventPublisher {
    public void publishLeadEvent(BaseEvent event)
    public void publishOpportunityEvent(BaseEvent event)
}
```

## Business Logic

### Lead Lifecycle
1. **NEW**: Initial lead creation
2. **CONTACTED**: First contact made
3. **QUALIFIED**: Lead meets criteria
4. **PROPOSAL**: Proposal sent
5. **NEGOTIATION**: In negotiation phase
6. **CLOSED_WON**: Successfully converted
7. **CLOSED_LOST**: Unsuccessful conversion

### Opportunity Stages
1. **PROSPECTING**: Initial opportunity identification
2. **QUALIFICATION**: Opportunity qualification
3. **PROPOSAL**: Proposal development
4. **NEGOTIATION**: Contract negotiation
5. **CLOSED_WON**: Successfully closed
6. **CLOSED_LOST**: Unsuccessfully closed

### Lead Conversion Process
1. **Lead Creation**: Capture lead information
2. **Lead Qualification**: Assess lead quality
3. **Opportunity Creation**: Convert qualified lead to opportunity
4. **Customer Creation**: Convert won opportunity to customer

## Configuration

### application.yml
```yaml
server:
  port: 8082

spring:
  application:
    name: sales-service
  datasource:
    url: jdbc:postgresql://localhost:5432/crm_sales_db
  jpa:
    hibernate:
      ddl-auto: update
```

## Testing

### Create Lead
```bash
curl -X POST http://localhost:8082/api/v1/leads \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Tech Startup Lead",
    "email": "lead@techstartup.com",
    "phone": "+1234567890",
    "company": "Tech Startup Inc",
    "status": "NEW",
    "source": "WEBSITE",
    "assignedTo": "john.doe",
    "description": "Interested in our CRM solution"
  }'
```

### Create Opportunity
```bash
curl -X POST http://localhost:8082/api/v1/opportunities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Enterprise CRM Deal",
    "customerId": 1,
    "leadId": 1,
    "amount": 50000.00,
    "stage": "PROPOSAL",
    "type": "NEW_BUSINESS",
    "assignedTo": "john.doe",
    "description": "Large enterprise CRM implementation"
  }'
```

## Integration with Other Services

### Customer Service Integration
- **Lead Conversion**: Convert leads to customers
- **Customer Reference**: Link opportunities to customers
- **Event Publishing**: Notify customer service of conversions

### Task Service Integration
- **Follow-up Tasks**: Create tasks for lead follow-ups
- **Opportunity Tasks**: Create tasks for opportunity management
- **Event Publishing**: Notify task service of new leads/opportunities

### Notification Service Integration
- **Lead Notifications**: Notify assigned users of new leads
- **Opportunity Updates**: Notify stakeholders of opportunity changes
- **Event Publishing**: Publish lead and opportunity events

## Business Rules

### Lead Assignment
- **Auto-assignment**: Based on territory or workload
- **Manual assignment**: Sales manager assignment
- **Workload balancing**: Distribute leads evenly

### Opportunity Management
- **Stage progression**: Enforce stage progression rules
- **Amount tracking**: Track opportunity values
- **Win/loss analysis**: Analyze conversion rates

### Data Validation
- **Email validation**: Valid email format required
- **Phone validation**: Valid phone number format
- **Company validation**: Company name required
- **Amount validation**: Positive amounts only

## Monitoring and Analytics

### Key Metrics
- **Lead Conversion Rate**: Percentage of leads converted to opportunities
- **Opportunity Win Rate**: Percentage of opportunities won
- **Sales Pipeline Value**: Total value of opportunities in pipeline
- **Average Deal Size**: Average opportunity amount

### Reporting
- **Lead Reports**: Lead source analysis, conversion rates
- **Opportunity Reports**: Pipeline analysis, win/loss rates
- **Sales Performance**: Individual and team performance metrics

## Future Enhancements

### Planned Features
1. **Lead Scoring**: Automated lead qualification
2. **Sales Forecasting**: Predictive sales analytics
3. **Territory Management**: Geographic sales territory assignment
4. **Commission Tracking**: Sales commission calculation
5. **Quote Management**: Automated quote generation

### Scalability Considerations
1. **Lead Routing**: Intelligent lead assignment
2. **Performance Optimization**: Database query optimization
3. **Caching**: Frequently accessed data caching
4. **Load Balancing**: Multiple service instances 