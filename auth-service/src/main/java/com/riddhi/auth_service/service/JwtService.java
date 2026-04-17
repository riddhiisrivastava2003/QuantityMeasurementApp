package com.riddhi.auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

//Login / OAuth Success
//        ↓
//JwtService.generateToken()
//        ↓
//Token client ko milta hai
//        ↓
//Client → API call (Bearer token)
//        ↓
//JwtFilter
//        ↓
// JwtService.validateToken()

@Service
public class JwtService {

    //JwtService JWT tokens ko generate,
    // parse aur validate karta hai jisme subject-based identity,
    // expiration control aur HMAC-based signing use hota hai to ensure
    // secure stateless authentication.

    // SECRET key — same honi chahiye auth-service aur api-gateway dono mein
    private final String SECRET = "myveryveryverylongsecretkey123456789";
    // Key object create karo SECRET se — HMAC-SHA ke liye minimum 256 bits chahiye
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email) { //Ye login ke baad call hota hai
        return Jwts.builder() //Token banana start
                .setSubject(email) // 'sub' claim: username
                .setIssuedAt(new Date()) // 'iat' claim: abhi ka time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //1 ghanta
                .signWith(key) // HMAC-SHA256 sign karo
                .compact(); //output->abc.def.xyz

        //JWT STRUCTURE
        //HEADER.PAYLOAD.SIGNATURE
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder() .setSigningKey(key) // verify karne ke liye same key
        .build()
                .parseClaimsJws(token) // SignatureException agar invalid
         .getBody()
                .getSubject(); }

        //Token se user nikalta hai

    public boolean validateToken(String token, String email) {
        try {
            return extractUsername(token).equals(email) && !isTokenExpired(token);
            //Username match
            //Token expired nahi
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}