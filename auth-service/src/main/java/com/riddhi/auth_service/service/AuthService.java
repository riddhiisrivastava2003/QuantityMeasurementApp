package com.riddhi.auth_service.service;

import com.riddhi.auth_service.entity.User;
import com.riddhi.auth_service.repository.UserRepository;
import com.riddhi.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtService jwtService;

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 🔥 IMPORTANT FIX
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return jwtService.generateToken(user.getUsername());
    }

    public void logout(String token) {
        // TODO: store token in blacklist (Redis later)
        System.out.println("Logout successful, token: " + token);
    }
}