package com.bridgelabz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    public String getUsername() {
        return "";
    }

    public Object getPassword() {
        return "";
    }
}