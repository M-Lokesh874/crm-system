# Phase 4: Auth Service Implementation

## Overview
The Auth Service provides authentication and authorization functionality for the CRM system using JWT tokens and Spring Security.

## Architecture

### Service Details
- **Port**: 8085
- **Database**: PostgreSQL (crm_auth_db)
- **Discovery**: Eureka Client
- **Security**: Spring Security with JWT

### Key Components

#### 1. User Entity
```java
@Entity
@Table(name = "users")
public class User implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
    // ... other fields
}
```

#### 2. Authentication Endpoints
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `POST /api/v1/auth/refresh` - Refresh JWT token
- `POST /api/v1/auth/logout` - Logout user
- `GET /api/v1/auth/validate` - Validate JWT token
- `GET /api/v1/auth/profile` - Get user profile

#### 3. Security Configuration
- **Password Encoding**: BCrypt
- **Session Management**: Stateless
- **JWT Token**: Custom implementation with refresh tokens
- **Role-Based Access**: ADMIN, SALES_MANAGER, SALES_REP, SUPPORT_REP

## Implementation Details

### JWT Token Management
```java
@Component
public class JwtUtil {
    public String createToken(Map<String, Object> claims, String subject)
    public String createRefreshToken(String subject)
    public String extractUsername(String token)
    public Boolean validateToken(String token)
}
```

### Authentication Flow
1. **Register**: Create user with encrypted password
2. **Login**: Authenticate credentials, generate JWT token
3. **Validate**: Check token validity and user existence
4. **Refresh**: Generate new token using refresh token

### User Roles
- **ADMIN**: Full system access
- **SALES_MANAGER**: Sales management and reporting
- **SALES_REP**: Customer and lead management
- **SUPPORT_REP**: Support ticket management

## Configuration

### application.yml
```yaml
server:
  port: 8085

spring:
  application:
    name: auth-service
  datasource:
    url: jdbc:postgresql://localhost:5432/crm_auth_db
  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: your-secret-key-here
  expiration: 3600000 # 1 hour
  refresh-expiration: 86400000 # 24 hours
```

### Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Stateless session management
    // JWT token validation
    // Role-based authorization
}
```

## Testing

### Register User
```bash
curl -X POST http://localhost:8085/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@company.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "SALES_REP"
  }'
```

### Login User
```bash
curl -X POST http://localhost:8085/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123"
  }'
```

## Integration with Other Services

### JWT Token Usage
All other services use the JWT token from Auth Service for:
- **Authentication**: Validate user identity
- **Authorization**: Check user roles and permissions
- **User Context**: Extract user information from token

### Service Communication
- **Event Publishing**: User creation/update events
- **Token Validation**: Centralized token validation
- **User Management**: Centralized user data

## Security Considerations

### Password Security
- **Encryption**: BCrypt password hashing
- **Validation**: Password strength requirements
- **Storage**: Encrypted passwords only

### Token Security
- **Expiration**: Configurable token expiration
- **Refresh**: Secure refresh token mechanism
- **Validation**: Comprehensive token validation

### Access Control
- **Role-Based**: Granular role permissions
- **Endpoint Protection**: Secure API endpoints
- **CORS**: Cross-origin request handling

## Monitoring and Logging

### Health Checks
- **Actuator**: Built-in health endpoints
- **Database**: Connection health monitoring
- **Security**: Authentication status

### Logging
- **Authentication Events**: Login/logout tracking
- **Security Events**: Failed authentication attempts
- **Performance**: Token generation and validation metrics

## Future Enhancements

### Planned Features
1. **Multi-Factor Authentication**: SMS/Email verification
2. **OAuth Integration**: Social login providers
3. **Token Blacklisting**: Secure logout mechanism
4. **Password Reset**: Email-based password recovery
5. **Session Management**: Active session tracking

### Scalability Considerations
1. **Token Storage**: Redis for token caching
2. **Load Balancing**: Multiple auth service instances
3. **Database Sharding**: User data distribution
4. **Caching**: User profile caching 