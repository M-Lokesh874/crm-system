package com.org.crm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * CRM Gateway Application
 *
 * This service acts as the API Gateway for the CRM system.
 * It routes requests to backend services, handles authentication,
 * and provides cross-cutting concerns like rate limiting and monitoring.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CrmGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmGatewayApplication.class, args);
    }
}