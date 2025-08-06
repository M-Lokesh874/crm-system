package com.org.crm.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for Auth Service
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM Auth Service API")
                        .description("Authentication and Authorization Service for CRM System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CRM Team")
                                .email("support@crm.com")
                                .url("https://crm.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8085")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.crm.com")
                                .description("Production Server")
                ));
    }
} 