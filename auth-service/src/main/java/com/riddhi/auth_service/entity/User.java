package com.riddhi.auth_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    //users
    //----------------------------------
    //id (PK)
    //username (unique)
    //password
    //role
    //email (unique)
    //provider
    //createdAt

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;

    // email — populated for Google OAuth users
    @Column(unique = true)
    private String email;

    // oauth provider: "local" | "google"
    private String provider;

    private LocalDateTime createdAt;
}