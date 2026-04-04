package com.riddhi.quantity_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI quantityServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QMA — Quantity Service API")
                        .description("""
                                **Unit Conversion & Arithmetic Operations API**
                                
                                Supported categories: **Length · Weight · Temperature · Volume**
                                
                                ### Conversion
                                - `POST /api/quantity/convert` — convert a value between any two compatible units (public, no auth needed)
                                
                                ### Arithmetic (requires JWT)
                                - `POST /api/quantity/arithmetic/add` — add two quantities (auto-converts units)
                                - `POST /api/quantity/arithmetic/subtract` — subtract Q2 from Q1
                                - `POST /api/quantity/arithmetic/multiply` — multiply quantity by scalar
                                - `POST /api/quantity/arithmetic/divide` — divide quantity by scalar
                                - `POST /api/quantity/arithmetic/compare` — returns GREATER / LESS / EQUAL
                                
                                ### Supported Units
                                | Category | Units |
                                |---|---|
                                | Length | FEET, INCHES, YARD, METER, CENTIMETER, KILOMETER, MILLIMETER, MILE |
                                | Weight | GRAM, KILOGRAM, POUND, OUNCE, TON |
                                | Temperature | CELSIUS, FAHRENHEIT, KELVIN |
                                | Volume | LITER, MILLILITER, GALLON, CUP |
                                """)
                        .version("2.0.0")
                        .contact(new Contact().name("QMA Team")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Quantity Service (direct)"),
                        new Server().url("http://localhost:8080").description("Via API Gateway")
                ))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT from /api/auth/login on auth-service (port 8082)")));
    }
}
