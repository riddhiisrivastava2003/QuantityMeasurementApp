package com.riddhi.auth_service.config;

import com.riddhi.auth_service.security.JwtFilter;
import com.riddhi.auth_service.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {

        http
                // 🔥 Disable CSRF (important for APIs)
                .csrf(AbstractHttpConfigurer::disable)

                // 🔥 Disable form login (prevents HTML login page)
                .formLogin(AbstractHttpConfigurer::disable)

                // 🔥 Disable basic auth (prevents browser popup/login)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 🔥 Make session stateless (JWT based)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // 🔥 Return JSON instead of HTML login page
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                )

                // 🔥 Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 🔥 OAuth2 login handling
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler(jwtService))
                );

        // 🔥 Add JWT filter before Spring Security filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔥 OAuth2 success handler → generates JWT instead of redirecting
    @Bean
    public AuthenticationSuccessHandler successHandler(JwtService jwtService) {
        return (request, response, authentication) -> {

            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            String email = oauthUser.getAttribute("email");

            // 🔥 Generate JWT token
            String token = jwtService.generateToken(email);

            // 🔥 Return JSON response (NO HTML redirect)
            response.setContentType("application/json");
            response.getWriter().write("{\"token\": \"" + token + "\"}");
        };
    }
}