package com.stayease.property_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("Initializing OpenAPI configuration for Property Service API");
        log.debug("Setting up OpenAPI with Bearer JWT security scheme");

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Property Service API")
                        .description("StayEase Property management service ")
                        .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for authentication")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));

        log.info("OpenAPI configuration initialized successfully with security scheme");
        return openAPI;
    }
}