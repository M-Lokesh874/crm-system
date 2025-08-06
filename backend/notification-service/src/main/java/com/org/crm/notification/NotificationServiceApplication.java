package com.org.crm.notification;

import com.org.crm.common.config.RabbitMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * Notification Service Application
 *
 * This service manages notifications for the CRM system.
 * It consumes events from RabbitMQ and provides REST APIs for notification queries.
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import(RabbitMQConfig.class)
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}