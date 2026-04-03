package com.riddhi.auth_service.config;

import com.riddhi.auth_service.entity.User;
import com.riddhi.auth_service.repository.UserRepository;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // OAuth2 needs session briefly for the callback flow
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/api/auth/login",
                            "/api/auth/signup",
                            "/api/auth/register",
                            "/api/auth/health",
                            "/oauth2/**",
                            "/login/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler(jwtService))
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000", "http://localhost:8080"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(JwtService jwtService) {
        return (request, response, authentication) -> {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            String email = oauthUser.getAttribute("email");
            String name  = oauthUser.getAttribute("name");

            // ── Upsert user in DB ──────────────────────────────────────
            // Find by email first; if not found, create a new user record
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                // Also check if username already taken
                String baseUsername = (name != null ? name.replaceAll("\\s+", "").toLowerCase() : "user");
                String username = baseUsername;
                int attempt = 1;
                while (userRepository.findByUsername(username).isPresent()) {
                    username = baseUsername + attempt++;
                }

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setPassword("");          // no password for OAuth users
                newUser.setRole("USER");
                newUser.setProvider("google");
                newUser.setCreatedAt(LocalDateTime.now());
                return userRepository.save(newUser);
            });

            // Generate JWT using the stored username (so profile lookup works)
            String token = jwtService.generateToken(user.getUsername());

            // Redirect frontend with token + username so AuthContext can set both
            response.sendRedirect(
                "http://localhost:5173/oauth-callback?token=" + token
                    + "&username=" + user.getUsername()
                    + "&email=" + email
            );
        };
    }
}
