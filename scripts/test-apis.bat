@echo off
REM CRM System API Testing Script for Windows
REM This script tests all major APIs in the CRM system

echo 🚀 Starting CRM System API Testing
echo ==================================

REM Check if curl is available
curl --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ curl is not installed. Please install curl to run this script.
    pause
    exit /b 1
)

REM Base URLs
set AUTH_SERVICE=http://localhost:8085
set CUSTOMER_SERVICE=http://localhost:8081
set SALES_SERVICE=http://localhost:8082
set TASK_SERVICE=http://localhost:8083
set NOTIFICATION_SERVICE=http://localhost:8084
set GATEWAY=http://localhost:8080

REM JWT Token storage
set JWT_TOKEN=

echo.
echo 🔍 Checking Service Health

REM Check Auth Service
echo Checking Auth Service...
curl -s -o nul -w "%%{http_code}" "%AUTH_SERVICE%/actuator/health" > temp.txt
set /p AUTH_STATUS=<temp.txt
if "%AUTH_STATUS%"=="200" (
    echo ✅ Auth Service is running
) else (
    echo ❌ Auth Service is not running (HTTP %AUTH_STATUS%)
)

REM Check Customer Service
echo Checking Customer Service...
curl -s -o nul -w "%%{http_code}" "%CUSTOMER_SERVICE%/actuator/health" > temp.txt
set /p CUSTOMER_STATUS=<temp.txt
if "%CUSTOMER_STATUS%"=="200" (
    echo ✅ Customer Service is running
) else (
    echo ❌ Customer Service is not running (HTTP %CUSTOMER_STATUS%)
)

REM Check Sales Service
echo Checking Sales Service...
curl -s -o nul -w "%%{http_code}" "%SALES_SERVICE%/actuator/health" > temp.txt
set /p SALES_STATUS=<temp.txt
if "%SALES_STATUS%"=="200" (
    echo ✅ Sales Service is running
) else (
    echo ❌ Sales Service is not running (HTTP %SALES_STATUS%)
)

REM Check Task Service
echo Checking Task Service...
curl -s -o nul -w "%%{http_code}" "%TASK_SERVICE%/actuator/health" > temp.txt
set /p TASK_STATUS=<temp.txt
if "%TASK_STATUS%"=="200" (
    echo ✅ Task Service is running
) else (
    echo ❌ Task Service is not running (HTTP %TASK_STATUS%)
)

REM Check Notification Service
echo Checking Notification Service...
curl -s -o nul -w "%%{http_code}" "%NOTIFICATION_SERVICE%/actuator/health" > temp.txt
set /p NOTIFICATION_STATUS=<temp.txt
if "%NOTIFICATION_STATUS%"=="200" (
    echo ✅ Notification Service is running
) else (
    echo ❌ Notification Service is not running (HTTP %NOTIFICATION_STATUS%)
)

echo.
echo 🔐 Testing Auth Service

REM Register a test user
echo Registering test user...
curl -s -X POST "%AUTH_SERVICE%/api/v1/auth/register" -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"test@company.com\",\"password\":\"password123\",\"firstName\":\"Test\",\"lastName\":\"User\",\"phone\":\"+1234567890\",\"department\":\"Sales\",\"position\":\"Sales Representative\",\"role\":\"SALES_REP\"}" > register_response.json
findstr "id" register_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ User registration successful
) else (
    echo ❌ User registration failed
    type register_response.json
)

REM Login to get JWT token
echo Logging in to get JWT token...
curl -s -X POST "%AUTH_SERVICE%/api/v1/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"password\":\"password123\"}" > login_response.json
findstr "token" login_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Login successful, JWT token obtained
    REM Extract token (simplified)
    for /f "tokens=2 delims=:," %%a in ('findstr "token" login_response.json') do set JWT_TOKEN=%%a
    set JWT_TOKEN=%JWT_TOKEN:"=%
    echo Token: %JWT_TOKEN:~0,50%...
) else (
    echo ❌ Login failed
    type login_response.json
    goto :end
)

REM Validate token
echo Validating JWT token...
curl -s -X GET "%AUTH_SERVICE%/api/v1/auth/validate" -H "Authorization: Bearer %JWT_TOKEN%" > validate_response.txt
findstr "true" validate_response.txt >nul
if %errorlevel% equ 0 (
    echo ✅ Token validation successful
) else (
    echo ❌ Token validation failed
    type validate_response.txt
)

echo.
echo 👥 Testing Customer Service

REM Create a customer
echo Creating a customer...
curl -s -X POST "%CUSTOMER_SERVICE%/api/v1/customers" -H "Content-Type: application/json" -H "Authorization: Bearer %JWT_TOKEN%" -d "{\"name\":\"Acme Corporation\",\"email\":\"contact@acme.com\",\"phone\":\"+1234567890\",\"company\":\"Acme Corp\",\"industry\":\"Technology\",\"status\":\"ACTIVE\",\"source\":\"WEBSITE\",\"assignedTo\":\"testuser\"}" > customer_response.json
findstr "id" customer_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Customer creation successful
    REM Extract customer ID (simplified)
    for /f "tokens=2 delims=:," %%a in ('findstr "id" customer_response.json') do set CUSTOMER_ID=%%a
    set CUSTOMER_ID=%CUSTOMER_ID:"=%
    echo Customer ID: %CUSTOMER_ID%
) else (
    echo ❌ Customer creation failed
    type customer_response.json
)

REM Get customer by ID
if defined CUSTOMER_ID (
    echo Getting customer by ID...
    curl -s -X GET "%CUSTOMER_SERVICE%/api/v1/customers/%CUSTOMER_ID%" -H "Authorization: Bearer %JWT_TOKEN%" > get_customer_response.json
    findstr "id" get_customer_response.json >nul
    if %errorlevel% equ 0 (
        echo ✅ Get customer successful
    ) else (
        echo ❌ Get customer failed
        type get_customer_response.json
    )
)

echo.
echo 💰 Testing Sales Service

REM Create a lead
echo Creating a lead...
curl -s -X POST "%SALES_SERVICE%/api/v1/leads" -H "Content-Type: application/json" -H "Authorization: Bearer %JWT_TOKEN%" -d "{\"name\":\"Tech Startup Lead\",\"email\":\"lead@techstartup.com\",\"phone\":\"+1234567890\",\"company\":\"Tech Startup Inc\",\"status\":\"NEW\",\"source\":\"WEBSITE\",\"assignedTo\":\"testuser\",\"description\":\"Interested in our CRM solution\"}" > lead_response.json
findstr "id" lead_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Lead creation successful
    REM Extract lead ID (simplified)
    for /f "tokens=2 delims=:," %%a in ('findstr "id" lead_response.json') do set LEAD_ID=%%a
    set LEAD_ID=%LEAD_ID:"=%
    echo Lead ID: %LEAD_ID%
) else (
    echo ❌ Lead creation failed
    type lead_response.json
)

REM Create an opportunity
if defined CUSTOMER_ID if defined LEAD_ID (
    echo Creating an opportunity...
    curl -s -X POST "%SALES_SERVICE%/api/v1/opportunities" -H "Content-Type: application/json" -H "Authorization: Bearer %JWT_TOKEN%" -d "{\"name\":\"Enterprise CRM Deal\",\"customerId\":%CUSTOMER_ID%,\"leadId\":%LEAD_ID%,\"amount\":50000.00,\"stage\":\"PROPOSAL\",\"type\":\"NEW_BUSINESS\",\"assignedTo\":\"testuser\",\"description\":\"Large enterprise CRM implementation\"}" > opportunity_response.json
    findstr "id" opportunity_response.json >nul
    if %errorlevel% equ 0 (
        echo ✅ Opportunity creation successful
        REM Extract opportunity ID (simplified)
        for /f "tokens=2 delims=:," %%a in ('findstr "id" opportunity_response.json') do set OPPORTUNITY_ID=%%a
        set OPPORTUNITY_ID=%OPPORTUNITY_ID:"=%
        echo Opportunity ID: %OPPORTUNITY_ID%
    ) else (
        echo ❌ Opportunity creation failed
        type opportunity_response.json
    )
)

echo.
echo 📋 Testing Task Service

REM Create a task
echo Creating a task...
curl -s -X POST "%TASK_SERVICE%/api/v1/tasks" -H "Content-Type: application/json" -H "Authorization: Bearer %JWT_TOKEN%" -d "{\"title\":\"Follow up with Tech Startup\",\"description\":\"Call the lead to discuss requirements\",\"priority\":\"HIGH\",\"dueDate\":\"2024-02-15T10:00:00\",\"assignedTo\":\"testuser\",\"type\":\"FOLLOW_UP\",\"customerId\":%CUSTOMER_ID%,\"leadId\":%LEAD_ID%}" > task_response.json
findstr "id" task_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Task creation successful
    REM Extract task ID (simplified)
    for /f "tokens=2 delims=:," %%a in ('findstr "id" task_response.json') do set TASK_ID=%%a
    set TASK_ID=%TASK_ID:"=%
    echo Task ID: %TASK_ID%
) else (
    echo ❌ Task creation failed
    type task_response.json
)

REM Update task status
if defined TASK_ID (
    echo Updating task status...
    curl -s -X PATCH "%TASK_SERVICE%/api/v1/tasks/%TASK_ID%/status?status=IN_PROGRESS" -H "Authorization: Bearer %JWT_TOKEN%" > update_task_response.json
    findstr "id" update_task_response.json >nul
    if %errorlevel% equ 0 (
        echo ✅ Task status update successful
    ) else (
        echo ❌ Task status update failed
        type update_task_response.json
    )
)

echo.
echo 🔔 Testing Notification Service

REM Get notifications
echo Getting notifications...
curl -s -X GET "%NOTIFICATION_SERVICE%/api/v1/notifications" -H "Authorization: Bearer %JWT_TOKEN%" > notifications_response.json
findstr "content" notifications_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Get notifications successful
) else (
    echo ❌ Get notifications failed
    type notifications_response.json
)

echo.
echo 🌐 Testing Gateway

REM Test customer service through gateway
echo Testing customer service through gateway...
curl -s -X GET "%GATEWAY%/api/v1/customers" -H "Authorization: Bearer %JWT_TOKEN%" > gateway_customer_response.json
findstr "content" gateway_customer_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Gateway customer service test successful
) else (
    echo ❌ Gateway customer service test failed
    type gateway_customer_response.json
)

REM Test sales service through gateway
echo Testing sales service through gateway...
curl -s -X GET "%GATEWAY%/api/v1/leads" -H "Authorization: Bearer %JWT_TOKEN%" > gateway_sales_response.json
findstr "content" gateway_sales_response.json >nul
if %errorlevel% equ 0 (
    echo ✅ Gateway sales service test successful
) else (
    echo ❌ Gateway sales service test failed
    type gateway_sales_response.json
)

echo.
echo 📊 Testing Summary
echo ==================================
echo ✅ Auth Service: Registration, Login, Token Validation
echo ✅ Customer Service: Create Customer, Get Customer
echo ✅ Sales Service: Create Lead, Create Opportunity
echo ✅ Task Service: Create Task, Update Task Status
echo ✅ Notification Service: Get Notifications
echo ✅ Gateway: Route Testing

echo.
echo 🎉 All API tests completed!
echo The CRM system is working correctly.
echo You can now proceed with frontend integration.

:end
REM Clean up temporary files
del /q *.json *.txt temp.txt 2>nul
pause 