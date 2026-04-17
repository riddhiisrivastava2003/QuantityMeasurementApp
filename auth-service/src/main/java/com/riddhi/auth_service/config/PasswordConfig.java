package com.riddhi.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
//“Password ko encode/verify karne ke liye BCrypt use karo”
//Signup
//  ↓
//passwordEncoder.encode(password)
//  ↓
//DB me hashed password save
//
//Login
//  ↓
//passwordEncoder.matches(raw, hashed)
//  ↓
//Verify


//Frontend
//  ↓
//API Gateway
//  ↓
//JwtAuthenticationFilter
//  ↓
//Auth Service
//  ↓
//SecurityConfig
//  ↓
//JwtFilter
//  ↓
//AuthController
//  ↓
//AuthService
//  ↓
//UserRepository
//  ↓
//Database