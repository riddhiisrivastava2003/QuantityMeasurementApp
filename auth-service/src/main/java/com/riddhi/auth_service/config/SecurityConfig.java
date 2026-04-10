//package com.riddhi.auth_service.config;
//
//import com.riddhi.auth_service.entity.User;
//import com.riddhi.auth_service.repository.UserRepository;
//import com.riddhi.auth_service.security.JwtFilter;
//import com.riddhi.auth_service.service.JwtService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired private JwtFilter jwtFilter;
//    @Autowired private UserRepository userRepository;
//
//    // Injected from application.properties → env var FRONTEND_URL
//    @Value("${frontend.url:http://localhost:5173}")
//    private String frontendUrl;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((req, res, e) -> {
//                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            res.setContentType("application/json");
//                            res.getWriter().write("{\"error\":\"Unauthorized\"}");
//                        })
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                            "/api/auth/login", "/api/auth/signup",
//                            "/api/auth/register", "/api/auth/health",
//                            "/oauth2/**", "/login/**"
//                        ).permitAll()
//                        .requestMatchers(
//                            "/v3/api-docs/**", "/swagger-ui/**",
//                            "/swagger-ui.html", "/swagger-resources/**", "/webjars/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth -> oauth.successHandler(successHandler(jwtService)));
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        // Allow all origins that include both local dev and production frontend
//        config.setAllowedOriginPatterns(List.of("*"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler successHandler(JwtService jwtService) {
//        return (request, response, authentication) -> {
//            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
//            String email = oauthUser.getAttribute("email");
//            String name  = oauthUser.getAttribute("name");
//
//            // Upsert: find by email → create if not found
//            User user = userRepository.findByEmail(email).orElseGet(() -> {
//                String base = (name != null ? name.replaceAll("\\s+", "").toLowerCase() : "user");
//                String username = base;
//                int attempt = 1;
//                while (userRepository.findByUsername(username).isPresent()) {
//                    username = base + attempt++;
//                }
//                User u = new User();
//                u.setUsername(username);
//                u.setEmail(email);
//                u.setPassword("");
//                u.setRole("USER");
//                u.setProvider("google");
//                u.setCreatedAt(LocalDateTime.now());
//                return userRepository.save(u);
//            });
//
//            String token = jwtService.generateToken(user.getUsername());
//
//            // Use FRONTEND_URL env var — works for both local and production
//            String redirectUrl = frontendUrl + "/oauth-callback"
//                    + "?token=" + token
//                    + "&username=" + user.getUsername()
//                    + "&email=" + (email != null ? email : "");
//
//            response.sendRedirect(redirectUrl);
//        };
//    }
//}

//
//package com.riddhi.auth_service.config;
//
//import com.riddhi.auth_service.entity.User;
//import com.riddhi.auth_service.repository.UserRepository;
//import com.riddhi.auth_service.security.JwtFilter;
//import com.riddhi.auth_service.service.JwtService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired private JwtFilter jwtFilter;
//    @Autowired private UserRepository userRepository;
//
//    // Injected from application.properties → env var FRONTEND_URL
//    @Value("${frontend.url:http://localhost:5173}")
//    private String frontendUrl;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((req, res, e) -> {
//                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            res.setContentType("application/json");
//                            res.getWriter().write("{\"error\":\"Unauthorized\"}");
//                        })
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/auth/login", "/api/auth/signup",
//                                "/api/auth/register", "/api/auth/health",
//                                "/oauth2/**", "/login/**"
//                        ).permitAll()
//                        .requestMatchers(
//                                "/v3/api-docs/**", "/swagger-ui/**",
//                                "/swagger-ui.html", "/swagger-resources/**", "/webjars/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth -> oauth.successHandler(successHandler(jwtService)));
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        // FIX: Use explicit allowedOrigins (not allowedOriginPatterns) with allowCredentials
//        // Also add API Gateway Render URL so it can forward requests here
//        config.setAllowedOrigins(List.of(
//                "http://localhost:5173",
//                "http://localhost:3000",
//                "https://quantity-measurement-app-frontend-puce.vercel.app",
//                "https://api-gateway-p6i7.onrender.com"
//        ));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler successHandler(JwtService jwtService) {
//        return (request, response, authentication) -> {
//            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
//            String email = oauthUser.getAttribute("email");
//            String name  = oauthUser.getAttribute("name");
//
//            // Upsert: find by email → create if not found
//            User user = userRepository.findByEmail(email).orElseGet(() -> {
//                String base = (name != null ? name.replaceAll("\\s+", "").toLowerCase() : "user");
//                String username = base;
//                int attempt = 1;
//                while (userRepository.findByUsername(username).isPresent()) {
//                    username = base + attempt++;
//                }
//                User u = new User();
//                u.setUsername(username);
//                u.setEmail(email);
//                u.setPassword("");
//                u.setRole("USER");
//                u.setProvider("google");
//                u.setCreatedAt(LocalDateTime.now());
//                return userRepository.save(u);
//            });
//
//            String token = jwtService.generateToken(user.getUsername());
//
//            // Use FRONTEND_URL env var — works for both local and production
//            String redirectUrl = frontendUrl + "/oauth-callback"
//                    + "?token=" + token
//                    + "&username=" + user.getUsername()
//                    + "&email=" + (email != null ? email : "");
//
//            response.sendRedirect(redirectUrl);
//        };
//    }
//}


package com.riddhi.auth_service.config;

import com.riddhi.auth_service.entity.User;
import com.riddhi.auth_service.repository.UserRepository;
import com.riddhi.auth_service.security.JwtFilter;
import com.riddhi.auth_service.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired private JwtFilter jwtFilter;
    @Autowired private UserRepository userRepository;

    // Injected from application.properties → env var FRONTEND_URL
    @Value("${frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login", "/api/auth/signup",
                                "/api/auth/register", "/api/auth/health",
                                "/oauth2/**", "/login/**"
                        ).permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**",
                                "/swagger-ui.html", "/swagger-resources/**", "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth.successHandler(successHandler(jwtService)));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(JwtService jwtService) {
        return (request, response, authentication) -> {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            String name  = oauthUser.getAttribute("name");

            // Upsert: find by email → create if not found
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                String base = (name != null ? name.replaceAll("\\s+", "").toLowerCase() : "user");
                String username = base;
                int attempt = 1;
                while (userRepository.findByUsername(username).isPresent()) {
                    username = base + attempt++;
                }
                User u = new User();
                u.setUsername(username);
                u.setEmail(email);
                u.setPassword("");
                u.setRole("USER");
                u.setProvider("google");
                u.setCreatedAt(LocalDateTime.now());
                return userRepository.save(u);
            });

            String token = jwtService.generateToken(user.getUsername());

            // Use FRONTEND_URL env var — works for both local and production
            String redirectUrl = frontendUrl + "/oauth-callback"
                    + "?token=" + token
                    + "&username=" + user.getUsername()
                    + "&email=" + (email != null ? email : "");

            response.sendRedirect(redirectUrl);
        };
    }
}
