package com.riddhi.auth_service.dto;

public class UserDTO {
    //Client ko limited/safe data dena

    //Direct entity return karoge toh:
    //password leak ho sakta hai
    //extra data expose ho sakta hai

    private String username;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

//POST /login
//  ↓
//AuthController
//  ↓
//AuthService
//  ↓
//UserRepository.findByUsername()
//  ↓
//User (Entity from DB)
//  ↓
//Password match
//  ↓
//JwtService.generateToken()
//  ↓
//Response