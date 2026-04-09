package com.riddhi.api_gateway.filter;

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

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    // Public paths that don't need JWT
    private static final java.util.List<String> PUBLIC_PATHS = java.util.Arrays.asList(
        "/api/auth/login",
        "/api/auth/signup",
        "/api/auth/register",
        "/api/auth/health",
        "/oauth2/",
        "/login/oauth2/",
        "/actuator/"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        // Allow OPTIONS (preflight CORS requests)
        if (method == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // Allow public endpoints
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        if (isPublic) {
            return chain.filter(exchange);
        }

        // For quantity endpoints - allow unauthenticated access (public dashboard)
        // But still forward user info if token present
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // If no auth header on public quantity paths - allow through
        if (path.startsWith("/api/quantity/convert") || path.startsWith("/api/quantity/test")) {
            if (authHeader == null || authHeader.isEmpty()) {
                return chain.filter(exchange);
            }
        }

        // For protected paths - require JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.validateToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Forward username as header to downstream services
            String username = jwtUtil.extractUsername(token);
            ServerWebExchange mutated = exchange.mutate()
                    .request(r -> r.header("X-User-Name", username))
                    .build();
            return chain.filter(mutated);

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
}
