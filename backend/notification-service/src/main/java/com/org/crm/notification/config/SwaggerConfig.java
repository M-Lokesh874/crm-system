package com.org.crm.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for Notification Service
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM Notification Service API")
                        .description("REST API for managing notifications in the CRM system. Provides endpoints for notification queries, statistics, and management.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CRM Development Team")
                                .email("dev@crm-system.com")
                                .url("https://crm-system.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.crm-system.com")
                                .description("Production Server")
                ));
    }
}