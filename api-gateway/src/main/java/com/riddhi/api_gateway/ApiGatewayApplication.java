package com.riddhi.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication //App start + auto config + component scan
@EnableDiscoveryClient //Eureka se connect karta hai Services discover karta hai

//API Gateway microservices architecture me single entry point hota hai
// jo client requests ko appropriate microservice tak route karta hai,
// aur cross-cutting concerns jaise authentication, logging aur CORS handle karta hai
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}


//Client → API Gateway → Filter → Route → Microservice → Response → Gateway → Client