//package com.riddhi.auth_service.controller;
//
//import com.riddhi.auth_service.entity.User;
//import com.riddhi.auth_service.repository.UserRepository;
//import com.riddhi.auth_service.service.JwtService;
//import com.riddhi.auth_service.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtService jwtService;   // ✅ FIXED (inject properly)
//
//    // 🔐 LOGIN
//    @PostMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password) {
//        return authService.login(username, password);
//    }
//
//
//
//    // 📝 SIGNUP
//
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody User user) {
//
//        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
//
//        if (existingUser.isPresent()) {
//            return ResponseEntity.badRequest().body("Username already exists");
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    // 🚪 LOGOUT
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
//
//        String token = authHeader.substring(7);
//
//        authService.logout(token);
//
//        return ResponseEntity.ok("Logged out successfully");
//    }
//
//    // 👤 PROFILE
//    @GetMapping("/profile")
//    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String authHeader) {
//
//        String token = authHeader.substring(7);
//
//        // ✅ FIXED: use injected jwtUtil
//        String username = jwtUtil.extractUsername(token);
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return ResponseEntity.ok(user);
//    }
//
//    @GetMapping("/api/auth/profile")
//    public Object getProfile(Authentication authentication) {
//        return authentication.getPrincipal();
//    }
//}


package com.riddhi.auth_service.controller;

import com.riddhi.auth_service.entity.User;
import com.riddhi.auth_service.repository.UserRepository;
import com.riddhi.auth_service.service.AuthService;
import com.riddhi.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // 🔐 LOGIN
    @PostMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password) {
//        return authService.login(username, password);
//    }

    public String login(@RequestBody String username,
                        @RequestBody String password) {
        return authService.login(username, password);
    }

    // 📝 SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // 🚪 LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        authService.logout(token);

        return ResponseEntity.ok("Logged out successfully");
    }

    // 👤 PROFILE
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }
}