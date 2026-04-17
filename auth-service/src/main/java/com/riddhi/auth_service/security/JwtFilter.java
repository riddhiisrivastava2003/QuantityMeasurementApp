//package com.riddhi.auth_service.security;
//
//import com.riddhi.auth_service.service.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        // 🔍 DEBUG
//        System.out.println("Authorization Header: " + authHeader);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//
//            String token = authHeader.substring(7);
//
//            try {
//                String email = jwtService.extractUsername(token);
//
//                // 🔥 IMPORTANT CHECK (duplicate auth avoid)
//                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                    // 🔍 DEBUG
//                    System.out.println("JWT valid for user: " + email);
//                }
//
//            } catch (Exception e) {
//                System.out.println("JWT invalid: " + e.getMessage());
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}


package com.riddhi.auth_service.security;

import com.riddhi.auth_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

//Ye filter ka kaam hai:
//
// JWT ko read karna
// validate karna
// Spring Security ko batana: “user authenticated hai

//Gateway	Token validate (first check)
//Auth Service	Authentication set in Spring

//Gateway: “Gatekeeper”
//✔️ Auth Filter: “Login session creator

@Component
public class JwtFilter extends OncePerRequestFilter {

    // OncePerRequestFilter: har request mein exactly ek baar fire hota hai
    // Even agar request forward/redirect ho


    @Autowired
    private JwtService jwtService; //username nikalne ke liye token validate karne ke liye

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            //ye method har request pe run hota hai
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); //AUTH HEADER LO

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //Agar token nahi: skip next filter pe jao

            String token = authHeader.substring(7);

            try {
                // Check: SecurityContext mein already authentication set hai?
                // Agar hai toh dobara set mat karo (duplicate prevention)

                String email = jwtService.extractUsername(token); //username extrat

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) { //email extract
                    //Agar already authentication set hai: dobara set mat karo duplicate avoid

                    if (jwtService.validateToken(token, email)) { //TOKEN VALIDATE
                        // Authentication object banao aur SecurityContext mein daal do

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                        //isse Spring ko pata chalta hai: user logged in hai

                        SecurityContextHolder.getContext().setAuthentication(auth); //“User authenticated hai”
                    }
                }

            } catch (Exception e) {
                System.out.println("JWT error: " + e.getMessage());
                // Invalid token — silently fail, filter chain continue karo
                // authorizeHttpRequests() step pe request reject hogi

            }
        }

        filterChain.doFilter(request, response);
    }
}

//Request
//  ↓
//Check Authorization Header
//  ↓
//Extract Token
//  ↓
//Extract Username
//  ↓
//Validate Token
//  ↓
//Create Authentication
//  ↓
//Set SecurityContext
//  ↓
//Forward Request

//JwtFilter
//  ↓
//Extract token
//  ↓
//Username → riddhi
//  ↓
//Token valid ✔️
//  ↓
//SecurityContext set
//  ↓
//Controller access allowed