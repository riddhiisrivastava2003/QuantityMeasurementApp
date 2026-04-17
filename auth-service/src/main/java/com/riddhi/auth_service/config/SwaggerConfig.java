package com.riddhi.auth_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    //SwaggerConfig API documentation,
    // testing UI aur JWT-based authorization support provide karta hai
    // developers ke liye

    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QMA — Auth Service API")
                        .description("""
                                **Authentication & User Management API**
                                
                                Handles user registration, login, logout, profile and Google OAuth2.
                                
                                - **Login** → `POST /api/auth/login` → returns JWT token
                                - **Register** → `POST /api/auth/register`
                                - **Profile** → `GET /api/auth/profile` (requires Bearer token)
                                - **Google OAuth** → redirect browser to `/oauth2/authorization/google`
                                
                                Use the **Authorize** button to enter your JWT token for protected endpoints.
                                """)
                        .version("2.0.0")
                        .contact(new Contact().name("QMA Team").email("qma@example.com"))
                        .license(new License().name("MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Auth Service (direct)"),
                        new Server().url("http://localhost:8080").description("Via API Gateway")
                ))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token from /api/auth/login")));
    }
}
