package com.org.crm.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for the CRM system
 */
@Configuration
public class RabbitMQConfig {
    
    // Queue names
    public static final String CUSTOMER_EVENTS_QUEUE = "customer.events.queue";
    public static final String LEAD_EVENTS_QUEUE = "lead.events.queue";
    public static final String TASK_EVENTS_QUEUE = "task.events.queue";
    public static final String OPPORTUNITY_EVENTS_QUEUE = "opportunity.events.queue";
    public static final String USER_EVENTS_QUEUE = "user.events.user.registered";
    
    // Exchange names
    public static final String CRM_EVENTS_EXCHANGE = "crm.events.exchange";
    
    // Routing keys
    public static final String CUSTOMER_EVENTS_ROUTING_KEY = "customer.events.*";
    public static final String LEAD_EVENTS_ROUTING_KEY = "lead.events.*";
    public static final String TASK_EVENTS_ROUTING_KEY = "task.events.*";
    public static final String OPPORTUNITY_EVENTS_ROUTING_KEY = "opportunity.events.*";
    public static final String USER_EVENTS_ROUTING_KEY = "user.events.*";
    
    @Bean
    public Queue customerEventsQueue() {
        return new Queue(CUSTOMER_EVENTS_QUEUE, true);
    }
    
    @Bean
    public Queue leadEventsQueue() {
        return new Queue(LEAD_EVENTS_QUEUE, true);
    }
    
    @Bean
    public Queue taskEventsQueue() {
        return new Queue(TASK_EVENTS_QUEUE, true);
    }

    @Bean
    public Queue opportunityEventsQueue() {
        return new Queue(OPPORTUNITY_EVENTS_QUEUE, true);
    }

    @Bean
    public Queue userEventsQueue() {
        return new Queue(USER_EVENTS_QUEUE, true);
    }
    
    @Bean
    public TopicExchange crmEventsExchange() {
        return new TopicExchange(CRM_EVENTS_EXCHANGE);
    }
    
    @Bean
    public Binding customerEventsBinding() {
        return BindingBuilder
                .bind(customerEventsQueue())
                .to(crmEventsExchange())
                .with(CUSTOMER_EVENTS_ROUTING_KEY);
    }
    
    @Bean
    public Binding leadEventsBinding() {
        return BindingBuilder
                .bind(leadEventsQueue())
                .to(crmEventsExchange())
                .with(LEAD_EVENTS_ROUTING_KEY);
    }
    
    @Bean
    public Binding taskEventsBinding() {
        return BindingBuilder
                .bind(taskEventsQueue())
                .to(crmEventsExchange())
                .with(TASK_EVENTS_ROUTING_KEY);
    }
    
    @Bean
    public Binding opportunityEventsBinding() {
        return BindingBuilder
                .bind(opportunityEventsQueue())
                .to(crmEventsExchange())
                .with(OPPORTUNITY_EVENTS_ROUTING_KEY);
    }
    
    @Bean
    public Binding userEventsBinding() {
        return BindingBuilder
                .bind(userEventsQueue())
                .to(crmEventsExchange())
                .with(USER_EVENTS_ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 