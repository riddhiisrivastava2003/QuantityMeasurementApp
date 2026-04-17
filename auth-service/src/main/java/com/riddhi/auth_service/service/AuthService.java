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
    //AuthService user authentication ka core logic handle karta hai
    // jisme database se user fetch, encrypted password verification
    // aur successful validation ke baad JWT token generation
    // hota hai

    @Autowired
    private UserRepository userRepository; //db se user lena

    @Autowired
    private PasswordEncoder passwordEncoder; //password verify


    @Autowired
    private JwtService jwtService; //token generate

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // IMPORTANT FIX
        if (!passwordEncoder.matches(password, user.getPassword())) {
            //Signup me password encode hua tha:
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return jwtService.generateToken(user.getUsername());
        //username → JwtService → JWT token
    }

    public void logout(String token) {
        // TODO: store token in blacklist (Redis later)
        System.out.println("Logout successful, token: " + token);

        //token blacklist me store
        // future requests reject
    }
    //JWT stateless hota hai → server store nahi karta
}

//POST /api/auth/login
//  ↓
//AuthController
//  ↓
//AuthService.login()
//  ↓
//UserRepository → DB check
//  ↓
//PasswordEncoder.matches()
//  ↓
//JwtService.generateToken()
//  ↓
//Controller response
//  ↓
//Client gets token