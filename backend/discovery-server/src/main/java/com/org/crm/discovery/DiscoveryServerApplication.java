package com.org.crm.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Discovery Server Application
 * 
 * This service provides service registration and discovery for the CRM system.
 * It uses Netflix Eureka Server to manage service instances and provide
 * load balancing capabilities.
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
} 