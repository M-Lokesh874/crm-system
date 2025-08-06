package com.org.crm.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Sales Service Application
 *
 * This service manages sales data and operations for the CRM system.
 * It provides REST APIs for lead management, opportunity tracking,
 * sales pipeline management, and sales analytics. The service also
 * publishes events for sales lifecycle changes to other services
 * in the system.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class SalesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesServiceApplication.class, args);
    }
} 