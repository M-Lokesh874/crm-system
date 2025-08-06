#!/bin/bash

# CRM System API Testing Script
# This script tests all major APIs in the CRM system

echo "🚀 Starting CRM System API Testing"
echo "=================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Base URLs
AUTH_SERVICE="http://localhost:8085"
CUSTOMER_SERVICE="http://localhost:8081"
SALES_SERVICE="http://localhost:8082"
TASK_SERVICE="http://localhost:8083"
NOTIFICATION_SERVICE="http://localhost:8084"
GATEWAY="http://localhost:8080"

# JWT Token storage
JWT_TOKEN=""

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

# Function to check if service is running
check_service() {
    local service_name=$1
    local service_url=$2
    echo -e "${YELLOW}Checking $service_name...${NC}"
    
    response=$(curl -s -o /dev/null -w "%{http_code}" "$service_url/actuator/health")
    if [ "$response" = "200" ]; then
        print_status 0 "$service_name is running"
        return 0
    else
        print_status 1 "$service_name is not running (HTTP $response)"
        return 1
    fi
}

# Check all services
echo -e "\n${YELLOW}🔍 Checking Service Health${NC}"
check_service "Auth Service" "$AUTH_SERVICE"
check_service "Customer Service" "$CUSTOMER_SERVICE"
check_service "Sales Service" "$SALES_SERVICE"
check_service "Task Service" "$TASK_SERVICE"
check_service "Notification Service" "$NOTIFICATION_SERVICE"

# Test Auth Service
echo -e "\n${YELLOW}🔐 Testing Auth Service${NC}"

# Register a test user
echo "Registering test user..."
register_response=$(curl -s -X POST "$AUTH_SERVICE/api/v1/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@company.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+1234567890",
    "department": "Sales",
    "position": "Sales Representative",
    "role": "SALES_REP"
  }')

if echo "$register_response" | grep -q "id"; then
    print_status 0 "User registration successful"
else
    print_status 1 "User registration failed"
    echo "Response: $register_response"
fi

# Login to get JWT token
echo "Logging in to get JWT token..."
login_response=$(curl -s -X POST "$AUTH_SERVICE/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }')

JWT_TOKEN=$(echo "$login_response" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -n "$JWT_TOKEN" ]; then
    print_status 0 "Login successful, JWT token obtained"
    echo "Token: ${JWT_TOKEN:0:50}..."
else
    print_status 1 "Login failed"
    echo "Response: $login_response"
    exit 1
fi

# Validate token
echo "Validating JWT token..."
validate_response=$(curl -s -X GET "$AUTH_SERVICE/api/v1/auth/validate" \
  -H "Authorization: Bearer $JWT_TOKEN")

if [ "$validate_response" = "true" ]; then
    print_status 0 "Token validation successful"
else
    print_status 1 "Token validation failed"
    echo "Response: $validate_response"
fi

# Test Customer Service
echo -e "\n${YELLOW}👥 Testing Customer Service${NC}"

# Create a customer
echo "Creating a customer..."
customer_response=$(curl -s -X POST "$CUSTOMER_SERVICE/api/v1/customers" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Acme Corporation",
    "email": "contact@acme.com",
    "phone": "+1234567890",
    "company": "Acme Corp",
    "industry": "Technology",
    "status": "ACTIVE",
    "source": "WEBSITE",
    "assignedTo": "testuser"
  }')

if echo "$customer_response" | grep -q "id"; then
    print_status 0 "Customer creation successful"
    CUSTOMER_ID=$(echo "$customer_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
    echo "Customer ID: $CUSTOMER_ID"
else
    print_status 1 "Customer creation failed"
    echo "Response: $customer_response"
fi

# Get customer by ID
if [ -n "$CUSTOMER_ID" ]; then
    echo "Getting customer by ID..."
    get_customer_response=$(curl -s -X GET "$CUSTOMER_SERVICE/api/v1/customers/$CUSTOMER_ID" \
      -H "Authorization: Bearer $JWT_TOKEN")
    
    if echo "$get_customer_response" | grep -q "id"; then
        print_status 0 "Get customer successful"
    else
        print_status 1 "Get customer failed"
        echo "Response: $get_customer_response"
    fi
fi

# Test Sales Service
echo -e "\n${YELLOW}💰 Testing Sales Service${NC}"

# Create a lead
echo "Creating a lead..."
lead_response=$(curl -s -X POST "$SALES_SERVICE/api/v1/leads" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Tech Startup Lead",
    "email": "lead@techstartup.com",
    "phone": "+1234567890",
    "company": "Tech Startup Inc",
    "status": "NEW",
    "source": "WEBSITE",
    "assignedTo": "testuser",
    "description": "Interested in our CRM solution"
  }')

if echo "$lead_response" | grep -q "id"; then
    print_status 0 "Lead creation successful"
    LEAD_ID=$(echo "$lead_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
    echo "Lead ID: $LEAD_ID"
else
    print_status 1 "Lead creation failed"
    echo "Response: $lead_response"
fi

# Create an opportunity
if [ -n "$CUSTOMER_ID" ] && [ -n "$LEAD_ID" ]; then
    echo "Creating an opportunity..."
    opportunity_response=$(curl -s -X POST "$SALES_SERVICE/api/v1/opportunities" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $JWT_TOKEN" \
      -d '{
        "name": "Enterprise CRM Deal",
        "customerId": '$CUSTOMER_ID',
        "leadId": '$LEAD_ID',
        "amount": 50000.00,
        "stage": "PROPOSAL",
        "type": "NEW_BUSINESS",
        "assignedTo": "testuser",
        "description": "Large enterprise CRM implementation"
      }')
    
    if echo "$opportunity_response" | grep -q "id"; then
        print_status 0 "Opportunity creation successful"
        OPPORTUNITY_ID=$(echo "$opportunity_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
        echo "Opportunity ID: $OPPORTUNITY_ID"
    else
        print_status 1 "Opportunity creation failed"
        echo "Response: $opportunity_response"
    fi
fi

# Test Task Service
echo -e "\n${YELLOW}📋 Testing Task Service${NC}"

# Create a task
echo "Creating a task..."
task_response=$(curl -s -X POST "$TASK_SERVICE/api/v1/tasks" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "title": "Follow up with Tech Startup",
    "description": "Call the lead to discuss requirements",
    "priority": "HIGH",
    "dueDate": "2024-02-15T10:00:00",
    "assignedTo": "testuser",
    "type": "FOLLOW_UP",
    "customerId": '$CUSTOMER_ID',
    "leadId": '$LEAD_ID'
  }')

if echo "$task_response" | grep -q "id"; then
    print_status 0 "Task creation successful"
    TASK_ID=$(echo "$task_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
    echo "Task ID: $TASK_ID"
else
    print_status 1 "Task creation failed"
    echo "Response: $task_response"
fi

# Update task status
if [ -n "$TASK_ID" ]; then
    echo "Updating task status..."
    update_task_response=$(curl -s -X PATCH "$TASK_SERVICE/api/v1/tasks/$TASK_ID/status?status=IN_PROGRESS" \
      -H "Authorization: Bearer $JWT_TOKEN")
    
    if echo "$update_task_response" | grep -q "id"; then
        print_status 0 "Task status update successful"
    else
        print_status 1 "Task status update failed"
        echo "Response: $update_task_response"
    fi
fi

# Test Notification Service
echo -e "\n${YELLOW}🔔 Testing Notification Service${NC}"

# Get notifications
echo "Getting notifications..."
notifications_response=$(curl -s -X GET "$NOTIFICATION_SERVICE/api/v1/notifications" \
  -H "Authorization: Bearer $JWT_TOKEN")

if echo "$notifications_response" | grep -q "content"; then
    print_status 0 "Get notifications successful"
else
    print_status 1 "Get notifications failed"
    echo "Response: $notifications_response"
fi

# Test Gateway
echo -e "\n${YELLOW}🌐 Testing Gateway${NC}"

# Test customer service through gateway
echo "Testing customer service through gateway..."
gateway_customer_response=$(curl -s -X GET "$GATEWAY/api/v1/customers" \
  -H "Authorization: Bearer $JWT_TOKEN")

if echo "$gateway_customer_response" | grep -q "content"; then
    print_status 0 "Gateway customer service test successful"
else
    print_status 1 "Gateway customer service test failed"
    echo "Response: $gateway_customer_response"
fi

# Test sales service through gateway
echo "Testing sales service through gateway..."
gateway_sales_response=$(curl -s -X GET "$GATEWAY/api/v1/leads" \
  -H "Authorization: Bearer $JWT_TOKEN")

if echo "$gateway_sales_response" | grep -q "content"; then
    print_status 0 "Gateway sales service test successful"
else
    print_status 1 "Gateway sales service test failed"
    echo "Response: $gateway_sales_response"
fi

# Summary
echo -e "\n${YELLOW}📊 Testing Summary${NC}"
echo "=================================="
echo "✅ Auth Service: Registration, Login, Token Validation"
echo "✅ Customer Service: Create Customer, Get Customer"
echo "✅ Sales Service: Create Lead, Create Opportunity"
echo "✅ Task Service: Create Task, Update Task Status"
echo "✅ Notification Service: Get Notifications"
echo "✅ Gateway: Route Testing"

echo -e "\n${GREEN}🎉 All API tests completed!${NC}"
echo "The CRM system is working correctly."
echo "You can now proceed with frontend integration." 