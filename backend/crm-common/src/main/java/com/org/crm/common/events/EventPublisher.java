package com.org.crm.common.events;

import com.org.crm.common.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event publisher for the CRM system
 */
@Component
public class EventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * Publish customer events
     */
    public void publishCustomerEvent(BaseEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.CRM_EVENTS_EXCHANGE, 
                                       "customer.events." + event.getEventType(), event);
            logger.info("📤 Published customer event: {} - Event ID: {}", event.getEventType(), event.getEventId());
        } catch (Exception e) {
            logger.error("❌ Failed to publish customer event: {}", event.getEventType(), e);
        }
    }
    
    /**
     * Publish lead events
     */
    public void publishLeadEvent(BaseEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.CRM_EVENTS_EXCHANGE, 
                                       "lead.events." + event.getEventType(), event);
            logger.info("📤 Published lead event: {} - Event ID: {}", event.getEventType(), event.getEventId());
        } catch (Exception e) {
            logger.error("❌ Failed to publish lead event: {}", event.getEventType(), e);
        }
    }
    
    /**
     * Publish task events
     */
    public void publishTaskEvent(BaseEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.CRM_EVENTS_EXCHANGE, 
                                       "task.events." + event.getEventType(), event);
            logger.info("📤 Published task event: {} - Event ID: {}", event.getEventType(), event.getEventId());
        } catch (Exception e) {
            logger.error("❌ Failed to publish task event: {}", event.getEventType(), e);
        }
    }
    
    /**
     * Publish opportunity events
     */
    public void publishOpportunityEvent(BaseEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.CRM_EVENTS_EXCHANGE, 
                                       "opportunity.events." + event.getEventType(), event);
            logger.info("📤 Published opportunity event: {} - Event ID: {}", event.getEventType(), event.getEventId());
        } catch (Exception e) {
            logger.error("❌ Failed to publish opportunity event: {}", event.getEventType(), e);
        }
    }
    
    /**
     * Publish user events
     */
    public void publishUserEvent(BaseEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.CRM_EVENTS_EXCHANGE, 
                                       "user.events." + event.getEventType(), event);
            logger.info("📤 Published user event: {} - Event ID: {}", event.getEventType(), event.getEventId());
        } catch (Exception e) {
            logger.error("❌ Failed to publish user event: {}", event.getEventType(), e);
        }
    }
} 