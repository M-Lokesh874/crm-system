package com.org.crm.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Task Service Application
 *
 * This service manages tasks and activities for the CRM system.
 * It provides REST APIs for task CRUD, assignment, status updates,
 * and event publishing for task lifecycle changes.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}