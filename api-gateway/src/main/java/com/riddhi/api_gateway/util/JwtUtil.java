package com.riddhi.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

//JWT parse karta hai — validateToken() + extractUsername()

@Component //Spring bean hai
public class JwtUtil {

    //JwtFilter = “decision maker”
    //JwtUtil = “actual JWT ka kaam karne wala

    // ⚠ MUST MATCH auth-service JwtService secret exactly
    private final String SECRET = "myveryveryverylongsecretkey123456789";
    //Is project mein symmetric signing (HS256) use hua hai —
    // same key sign karne aur verify karne ke liye use hoti hai.
    // Dono services (auth-service aur api-gateway) mein SECRET same honi ZAROORI hai: warna
    // warna  Token invalid ho jayega
    //→ 401 Unauthorized
    // 'myveryveryverylongsecretkey123456789'.
    //HS256 (HMAC SHA-256) -> symmetric encryption Same key for sign + verify
    // Agar different ho toh gateway har token invalid mark karega.

    public boolean validateToken(String token) {
        try {
            //token original hai?
            //expire toh nahi?
            //tampered toh nahi
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            //parseClaimsJws(token)
            //
            //Token ko decode karti hai
            // Signature verify karti hai
            //Expiry check karti hai
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        //Token ke andar se username nikalta hai
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        //Token ka payload milta hai
        return claims.getSubject();
    }
}


//Client Request aayi GET /api/history/getAll Authorization: Bearer abc123
//  ↓
//LoggingFilter ➡ Request print
//  ↓
//JwtAuthenticationFilter Check public? → NO Check token? → YES
//  ↓
//    ├── Public → allow
//    ├── Quantity → optional
//    └── Protected
//           ↓
//        JwtUtil
//           ↓
//     validateToken() Token verify:valid → continue invalid → 401
//           ↓
//     extractUsername() "riddhi"
//           ↓
//     Add Header X-User-Name: riddhi
//  ↓
// forward to Microservice 200 OK
//  ↓
//Response ok
//  ↓
//LoggingFilter ⬅ Response print