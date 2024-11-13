package com.uniquindio.api_rest.infra.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.uniquindio.api_rest.infra.security.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDetailsImpl userDetails) {
        // Crear la fecha de expiración del token (1 hora)
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpiration);

        // Generar el token usando la librería de JWT
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("ingesis.uniquindio.edu.co")
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private DecodedJWT extractAllClaims(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiresAt();
    }
}
