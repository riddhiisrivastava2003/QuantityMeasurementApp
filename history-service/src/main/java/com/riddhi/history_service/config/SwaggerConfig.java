package com.riddhi.history_service.config;

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
    public OpenAPI historyServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QMA — History Service API")
                        .description("""
                                **User Conversion & Operation History API**
                                
                                Tracks every conversion and arithmetic operation performed by authenticated users.
                                
                                - `POST /api/history/save` — save a history entry
                                - `GET  /api/history/my`   — get current user's history (ordered by date desc)
                                - `DELETE /api/history/clear` — clear all history for current user
                                - `GET  /api/history/user/{username}` — get by username
                                - `GET  /api/history/all` — admin: all records
                                """)
                        .version("2.0.0")
                        .contact(new Contact().name("QMA Team")))
                .servers(List.of(
                        new Server().url("http://localhost:8083").description("History Service (direct)"),
                        new Server().url("http://localhost:8080").description("Via API Gateway")
                ))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
