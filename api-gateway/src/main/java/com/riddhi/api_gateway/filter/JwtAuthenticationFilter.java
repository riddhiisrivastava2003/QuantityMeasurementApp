package com.riddhi.api_gateway.filter;


//MOST IMPORTANT — JWT validate, X-User-Name forward, public paths skip

import com.riddhi.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//JwtAuthenticationFilter central security layer hai jo
// public routes ko bypass karta hai, protected routes pe JWT validate
// karta hai, aur authenticated user details ko downstream
// services tak propagate karta hai

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {


    //GlobalFilter → sab requests pe lagega
    //Ordered → execution priority control

    // Implements GlobalFilter → applies to ALL routes
    // Implements Ordered → controls filter execution sequence


    @Autowired
    private JwtUtil jwtUtil;

    // Public paths that don't need JWT
    private static final java.util.List<String> PUBLIC_PATHS = java.util.Arrays.asList(
        "/api/auth/login",
        "/api/auth/signup",
        "/api/auth/register",
        "/api/auth/health", //Load balancer check karta hai
        "/oauth2/",
        "/login/oauth2/",
        "/actuator/" //Spring Boot monitoring endpoints
            //Check app running hai ya nahi
            // Metrics, memory, status
            // DevOps / monitoring tools use karte hain
    );



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //Request se data nikalna
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        // Allow OPTIONS (preflight CORS requests)
        // STEP 1: OPTIONS allow karo — ye browser CORS preflight hain
        if (method == HttpMethod.OPTIONS) {
            //Browser pehle OPTIONS bhejta hai → tum allow kar rahe ho
            //Correct (warna CORS fail ho jata)
            return chain.filter(exchange);
        }

        // Allow public endpoints
        // STEP 2: Public paths allow karo — in pe token nahi chahiye
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        if (isPublic) {
            return chain.filter(exchange);
            //Agar request in paths me hai:
            //JWT nahi chahiye
            //direct allow
        }

        // For quantity endpoints - allow unauthenticated access (public dashboard)
        // But still forward user info if token present
        // STEP 3: Convert endpoint publicly accessible but token optional
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // If no auth header on public quantity paths - allow through

        if (path.startsWith("/api/quantity/convert") || path.startsWith("/api/quantity/test")) {
            if (authHeader == null || authHeader.isEmpty()) {
                return chain.filter(exchange); //allow without token
            }
        }

        // STEP 4: Protected path — Bearer token mandatory
        // For protected paths - require JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // STEP 5: Token validate karo
            if (!jwtUtil.validateToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }


            // STEP 6: Username extract karo aur header mein daal do

            // Forward username as header to downstream services
            String username = jwtUtil.extractUsername(token);
            ServerWebExchange mutated = exchange.mutate()
                    .request(r -> r.header("X-User-Name", username))
                    //Tum request me naya header add kar rahi ho:
                    .build();
            return chain.filter(mutated); //Ab request service pe jayegi

        } catch (Exception e) {
            System.out.println("JWT ERROR: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    //Request
    //  ↓
    //Check OPTIONS → allow
    //  ↓
    //Check Public → allow
    //  ↓
    //Check Quantity → optional token
    //  ↓
    //Check Protected
    //  ↓
    //Token present?
    //  ↓
    //Validate Token (JwtUtil)
    //  ↓
    //Extract Username
    //  ↓
    //Add Header
    //  ↓
    //Forward to Service
}
