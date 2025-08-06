package com.org.crm.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Customer Service Application
 *
 * This service manages customer data and operations for the CRM system.
 * It provides REST APIs for customer CRUD operations, search, filtering,
 * and statistics. The service also publishes events for customer lifecycle
 * changes to other services in the system.
 */
@SpringBootApplication(scanBasePackages = {"com.org.crm.common", "com.org.crm.customer"})
@EnableDiscoveryClient
@EnableCaching
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
} 