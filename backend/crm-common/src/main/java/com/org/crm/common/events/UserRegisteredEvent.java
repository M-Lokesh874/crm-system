package com.org.crm.common.events;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserRegisteredEvent extends BaseEvent {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDateTime registeredAt;
    
    public UserRegisteredEvent(Long userId, String username, String email, String firstName, 
                             String lastName, String fullName, LocalDateTime registeredAt) {
        super("USER_REGISTERED", "auth-service");
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.registeredAt = registeredAt;
    }
} 