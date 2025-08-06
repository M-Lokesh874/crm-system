package com.org.crm.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for all events in the CRM system.
 * Contains common fields that all events should have.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {
    private String eventId = UUID.randomUUID().toString();
    private String eventType;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String source;
    private String version = "1.0";
    
    public BaseEvent(String eventType, String source) {
        this.eventType = eventType;
        this.source = source;
    }
    
    // Explicit getter methods to ensure they are available
    public String getEventId() {
        return eventId;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSource() {
        return source;
    }
    
    public String getVersion() {
        return version;
    }
} 