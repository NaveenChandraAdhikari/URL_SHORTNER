package com.url.url_shortner.jwt;

import com.url.url_shortner.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

     // jwt utils is like jwt helper class
    //pass the token (you not passing username and password all over again and again to authenticated end point ) so we pass the token to authorization header
    //Authorization header - > Bearer <>TOKEN<>

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;


    public String getJwtFromHeader(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");

//        removing the bearer from token
        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
            return  bearerToken.substring(7);
        }
        return null;
    }

//    we accept the object of userdetailimpl because  i want to have roles and username embedded into the token when generating
    public String generateToken(UserDetailsImpl userDetails){

        String username= userDetails.getUsername();
        String roles=userDetails.getAuthorities().stream()
                .map(authhority->authhority.getAuthority())
                .collect(Collectors.joining(","));

//        return the jwt token which is signed
        return Jwts.builder()
                .setSubject(username)
                .claim("roles",roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime()+ jwtExpirationMs) ))
                .signWith(key())
                .compact();
    }




    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }



    public String getUsernameFromJwtToken(String token) {
        SecretKey key = (SecretKey) key();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            SecretKey key = (SecretKey) key();
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken); // Parses and validates the token
            return true; // Token is valid if no exceptions are thrown
        } catch (Exception e) {
            // Log the exception for debugging (optional)
            System.err.println("Invalid JWT Token: " + e.getMessage());
            return false; // Token is invalid
        }
    }



}

