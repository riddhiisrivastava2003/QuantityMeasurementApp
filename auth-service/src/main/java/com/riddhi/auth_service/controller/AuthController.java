package com.riddhi.auth_service.controller;

import com.riddhi.auth_service.dto.UserDTO;
import com.riddhi.auth_service.entity.User;
import com.riddhi.auth_service.repository.UserRepository;
import com.riddhi.auth_service.service.AuthService;
import com.riddhi.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // ✅ LOGIN - accepts JSON body
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password required"));
        }

        try {
            String token = authService.login(username, password);
            return ResponseEntity.ok(Map.of(
                "token", token,
                "username", username,
                "message", "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ SIGNUP/REGISTER - accepts JSON body
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password required"));
        }

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);

        // Auto-login after signup
        String token = jwtService.generateToken(username);
        return ResponseEntity.ok(Map.of(
            "token", token,
            "username", username,
            "message", "Registration successful"
        ));
    }

    // Alias for signup (frontend uses /register)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        return signup(body);
    }

    // ✅ LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // ✅ PROFILE
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                         @RequestHeader(value = "X-User-Name", required = false) String xUsername) {
        try {
            String username = null;

            // Try X-User-Name header (forwarded by gateway)
            if (xUsername != null && !xUsername.isEmpty()) {
                username = xUsername;
            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }

            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Don't expose password
            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole() != null ? user.getRole() : "USER",
                "createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : ""
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ HEALTH CHECK
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "Auth Service Running ✅"));
    }
}
