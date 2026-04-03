package com.riddhi.auth_service.controller;

import com.riddhi.auth_service.entity.User;
import com.riddhi.auth_service.repository.UserRepository;
import com.riddhi.auth_service.service.AuthService;
import com.riddhi.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null)
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password required"));
        try {
            String token = authService.login(username.trim(), password);
            User user = userRepository.findByUsername(username.trim()).orElseThrow();
            return ResponseEntity.ok(buildUserResponse(token, user));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // REGISTER (alias: signup)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        return signup(body);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email    = body.get("email");
        if (username == null || password == null)
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password required"));
        if (userRepository.findByUsername(username.trim()).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        if (email != null && !email.isBlank() && userRepository.findByEmail(email.trim()).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email != null ? email.trim() : null);
        user.setRole("USER");
        user.setProvider("local");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(buildUserResponse(token, user));
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer "))
            authService.logout(authHeader.substring(7));
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // PROFILE — supports both JWT header and gateway-forwarded X-User-Name
//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(
//            @RequestHeader(value = "Authorization", required = false) String authHeader,
//            @RequestHeader(value = "X-User-Name",   required = false) String xUsername) {
//        try {
//            String username = null;
//            if (xUsername != null && !xUsername.isEmpty()) {
//                username = xUsername;
//            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                username = jwtService.extractUsername(authHeader.substring(7));
//            }
//            if (username == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
//
//            User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
//            return ResponseEntity.ok(buildProfileMap(user));
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
//        }
//    }
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Name", required = false) String xUsername) {
        try {
            String username;

            if (xUsername != null && !xUsername.isEmpty()) {
                username = xUsername;
            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                username = jwtService.extractUsername(authHeader.substring(7));
            } else {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Unauthorized"));
            }

            // final variable for lambda
            final String finalUsername = username;

            User user = userRepository.findByUsername(finalUsername)
                    .orElseThrow(() ->
                            new RuntimeException("User not found: " + finalUsername));

            return ResponseEntity.ok(buildProfileMap(user));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "Auth Service Running"));
    }

    // ── helpers ─────────────────────────────────────────────────────────
    private Map<String, Object> buildUserResponse(String token, User user) {
        Map<String, Object> res = new HashMap<>(buildProfileMap(user));
        res.put("token", token);
        res.put("message", "Success");
        return res;
    }

    private Map<String, Object> buildProfileMap(User user) {
        Map<String, Object> m = new HashMap<>();
        m.put("id",        user.getId());
        m.put("username",  user.getUsername());
        m.put("email",     user.getEmail()    != null ? user.getEmail()    : "");
        m.put("role",      user.getRole()     != null ? user.getRole()     : "USER");
        m.put("provider",  user.getProvider() != null ? user.getProvider() : "local");
        m.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
        return m;
    }
}
