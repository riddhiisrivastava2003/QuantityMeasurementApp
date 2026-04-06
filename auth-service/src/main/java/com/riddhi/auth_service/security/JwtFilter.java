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
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//
//            String token = authHeader.substring(7);
//
//            try {
//                String email = jwtService.extractUsername(token);
//
//                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                    if (jwtService.validateToken(token, email)) {
//
//                        UsernamePasswordAuthenticationToken authentication =
//                                new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
//
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//                }
//
//            } catch (Exception e) {
//                System.out.println("JWT error: " + e.getMessage());
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                String email = jwtService.extractUsername(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    if (jwtService.validateToken(token, email)) {

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }

            } catch (Exception e) {
                System.out.println("JWT error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}