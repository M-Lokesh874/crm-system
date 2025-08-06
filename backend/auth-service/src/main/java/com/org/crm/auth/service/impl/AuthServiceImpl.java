package com.org.crm.auth.service.impl;

import com.org.crm.auth.dto.LoginRequest;
import com.org.crm.auth.dto.LoginResponse;
import com.org.crm.auth.dto.RegisterRequest;
import com.org.crm.auth.dto.RegisterResponse;
import com.org.crm.auth.exception.AuthException;
import com.org.crm.auth.model.Role;
import com.org.crm.auth.model.User;
import com.org.crm.auth.repository.RoleRepository;
import com.org.crm.auth.repository.UserRepository;
import com.org.crm.auth.service.AuthService;
import com.org.crm.common.events.EventPublisher;
import com.org.crm.common.events.UserRegisteredEvent;
import com.org.crm.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EventPublisher eventPublisher;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already exists");
        }

        // Get or create default role
        Role defaultRole = roleRepository.findByName("SALES_REP")
                .orElseGet(() -> createDefaultRole("SALES_REP", "Sales Representative"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .position(request.getPosition())
                .roles(new HashSet<>())
                .build();

        // Add default role
        user.addRole(defaultRole);

        User savedUser = userRepository.save(user);
        
        log.info("User registered successfully: {} with roles: {}", savedUser.getUsername(), savedUser.getRoles());
        
        // Publish user registration event
        UserRegisteredEvent userEvent = new UserRegisteredEvent(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getFirstName() + " " + savedUser.getLastName(),
                savedUser.getCreatedAt()
        );
        eventPublisher.publishUserEvent(userEvent);
        
        return RegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getPrimaryRole())
                .phone(savedUser.getPhone())
                .department(savedUser.getDepartment())
                .position(savedUser.getPosition())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user.getUsername());
            String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

            log.info("User logged in successfully: {}", user.getUsername());

            return LoginResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.expiration)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getPrimaryRole())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            throw new AuthException("Invalid username or password");
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            throw new AuthException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public LoginResponse refreshToken(String token) {
        try {
            String refreshToken = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(refreshToken);
            
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String newToken = jwtUtil.generateToken(username);
            String newRefreshToken = jwtUtil.createRefreshToken(username);

            return LoginResponse.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.expiration)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getPrimaryRole())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .build();

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new AuthException("Invalid refresh token");
        }
    }

    @Override
    public void logout(String token) {
        // In a stateless JWT implementation, logout is typically handled client-side
        // by removing the token. Server-side logout would require a token blacklist.
        log.info("User logout requested");
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwtToken);
            return jwtUtil.validateToken(jwtToken, username);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    @Override
    public RegisterResponse getProfile(String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwtToken);
            
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return RegisterResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getPrimaryRole())
                    .phone(user.getPhone())
                    .department(user.getDepartment())
                    .position(user.getPosition())
                    .createdAt(user.getCreatedAt())
                    .build();

        } catch (Exception e) {
            log.error("Profile retrieval failed", e);
            throw new AuthException("Invalid token");
        }
    }

    private Role createDefaultRole(String name, String description) {
        Role role = Role.builder()
                .name(name)
                .description(description)
                .build();
        return roleRepository.save(role);
    }
} 