package com.org.crm.auth.service;

import com.org.crm.auth.dto.LoginRequest;
import com.org.crm.auth.dto.LoginResponse;
import com.org.crm.auth.dto.RegisterRequest;
import com.org.crm.auth.dto.RegisterResponse;

/**
 * Service interface for authentication operations
 */
public interface AuthService {
    
    /**
     * Register a new user
     */
    RegisterResponse register(RegisterRequest request);
    
    /**
     * Login user
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * Refresh JWT token
     */
    LoginResponse refreshToken(String token);
    
    /**
     * Logout user
     */
    void logout(String token);
    
    /**
     * Validate JWT token
     */
    boolean validateToken(String token);
    
    /**
     * Get user profile
     */
    RegisterResponse getProfile(String token);
} 