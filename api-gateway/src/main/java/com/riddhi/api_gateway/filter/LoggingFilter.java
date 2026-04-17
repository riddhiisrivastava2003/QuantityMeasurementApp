package com.riddhi.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//Har request URI aur response status code console pe log karta hai

//Jaise hi request Gateway me aati hai → ye filter trigger hota hai
//LoggingFilter ek GlobalFilter hai jo har incoming request aur outgoing response ko log karta hai bina request flow ko modify kiye

@Component
public class LoggingFilter implements GlobalFilter {
    //GlobalFilter -> Ye filter har request pe chalega

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        //ServerWebExchange exchange Isme pura request + response data hota hai
        //request URL
        //headers
        //body
        //response

        //Ye method har request pe automatically call hota hai

        //GatewayFilterChain chain->e request ko next filter ya service tak bhejne ke liye use hota ha

        System.out.println("➡ Request: " + exchange.getRequest().getURI());
       // Ye har request ko log karta hai eg ➡ Request: http://localhost:8080/api/quantity/convert

        return chain.filter(exchange) //request ko aage bhejega warna request yahi ruk jayegi
                .then(Mono.fromRunnable(() ->
                        System.out.println("⬅ Response: " + exchange.getResponse().getStatusCode())
                        //Ye code tab chalega jab response wapas aayega
                ));
    }
}


//STEP 1  User fills username + password on LoginPage.jsx
//STEP 2  authAPI.login(username, password) called — services/api.js
//STEP 3  Axios sends: POST https://api-gateway.onrender.com/api/auth/login
//Headers: Content-Type: application/json
//STEP 4  API GATEWAY receives request:
//LoggingFilter  → logs "➡ Request: /api/auth/login"
//JwtAuthFilter  → checks path against PUBLIC_PATHS list
//         /api/auth/login IS in PUBLIC_PATHS → SKIP JWT check → pass through
//STEP 5  Auth Service — AuthController.login() runs:
//        AuthService.login() → userRepository.findByUsername()
//         passwordEncoder.matches(rawPwd, hashedPwd) → verify BCrypt
//         jwtService.generateToken(username) → create JWT (1 hour)
//Returns: { token, username, email, role, provider, id, createdAt }
//STEP 6  Response travels back through Gateway → Frontend
//STEP 7  AuthContext.jsx:
//        localStorage.setItem("qma_token", token)
//setUser({ username, email, role, ... })
//Navigate to /dashboard
