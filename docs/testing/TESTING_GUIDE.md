# CRM System Testing Guide

## Overview
This guide provides step-by-step instructions for testing all CRM services.

## Prerequisites
1. All services are built successfully
2. Docker and Docker Compose are installed
3. PostgreSQL, Redis, and RabbitMQ are running

## Service URLs
- **Discovery Server**: http://localhost:8761
- **Auth Service**: http://localhost:8085
- **Customer Service**: http://localhost:8081
- **Sales Service**: http://localhost:8082
- **Task Service**: http://localhost:8083
- **Notification Service**: http://localhost:8084
- **CRM Gateway**: http://localhost:8080

## Testing Sequence

### 1. Start All Services
```bash
cd crm-system
docker-compose up --build
```

### 2. Auth Service Testing

#### 2.1 Register User
```bash
curl -X POST http://localhost:8085/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@company.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "department": "Sales",
    "position": "Sales Representative",
    "role": "SALES_REP"
  }'
```

#### 2.2 Login User
```bash
curl -X POST http://localhost:8085/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123"
  }'
```

**Save the JWT token from the response for subsequent requests.**

#### 2.3 Validate Token
```bash
curl -X GET http://localhost:8085/api/v1/auth/validate \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Customer Service Testing

#### 3.1 Create Customer
```bash
curl -X POST http://localhost:8081/api/v1/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Acme Corporation",
    "email": "contact@acme.com",
    "phone": "+1234567890",
    "company": "Acme Corp",
    "industry": "Technology",
    "status": "ACTIVE",
    "source": "WEBSITE",
    "assignedTo": "john.doe"
  }'
```

#### 3.2 Get Customer by ID
```bash
curl -X GET http://localhost:8081/api/v1/customers/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Sales Service Testing

#### 4.1 Create Lead
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

#### 4.2 Create Opportunity
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

### 5. Task Service Testing

#### 5.1 Create Task
```bash
curl -X POST http://localhost:8083/api/v1/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Follow up with Tech Startup",
    "description": "Call the lead to discuss requirements",
    "priority": "HIGH",
    "dueDate": "2024-02-15T10:00:00",
    "assignedTo": "john.doe",
    "type": "FOLLOW_UP",
    "customerId": 1,
    "leadId": 1
  }'
```

#### 5.2 Update Task Status
```bash
curl -X PATCH http://localhost:8083/api/v1/tasks/1/status?status=IN_PROGRESS \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. Notification Service Testing

#### 6.1 Get Notifications
```bash
curl -X GET http://localhost:8084/api/v1/notifications \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 7. Gateway Testing

#### 7.1 Test Gateway Routes
```bash
# Test customer service through gateway
curl -X GET http://localhost:8080/api/v1/customers \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test sales service through gateway
curl -X GET http://localhost:8080/api/v1/leads \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Expected Results

### Auth Service
- ✅ Register: Returns 201 with user details
- ✅ Login: Returns 200 with JWT token
- ✅ Validate: Returns 200 with true/false

### Customer Service
- ✅ Create: Returns 201 with customer details
- ✅ Get: Returns 200 with customer details

### Sales Service
- ✅ Create Lead: Returns 201 with lead details
- ✅ Create Opportunity: Returns 201 with opportunity details

### Task Service
- ✅ Create Task: Returns 201 with task details
- ✅ Update Status: Returns 200 with updated task

### Notification Service
- ✅ Get Notifications: Returns 200 with notifications list

### Gateway
- ✅ Routes: Returns 200 for valid requests

## Troubleshooting

### Common Issues
1. **Database Connection**: Check if PostgreSQL is running
2. **Service Discovery**: Verify Eureka server is accessible
3. **JWT Token**: Ensure token is valid and not expired
4. **CORS**: Check if frontend can access APIs

### Health Checks
```bash
# Check service health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
```

### Logs
```bash
# View service logs
docker-compose logs auth-service
docker-compose logs customer-service
docker-compose logs sales-service
docker-compose logs task-service
docker-compose logs notification-service
``` 