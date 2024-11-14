package com.uniquindio.api_rest.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saludo")
public class SaludoController {

    private static final String SECRET_KEY = "mysecretkey"; // Clave secreta utilizada para firmar y verificar el JWT
    private static final String ISSUER = "ingesis.uniquindio.edu.co";
    @GetMapping
    public ResponseEntity<String> saludo(
            @RequestParam(required = false) String nombre,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        if (nombre==null || nombre.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solicitud no válida: El nombre es obligatorio");
        }

        // Verificar la cabecera Authorization
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Autenticación requerida: JWT no proporcionado o formato incorrecto");
        }

        String token = authorizationHeader.substring(7); // Remover "Bearer " del token

        try {
            // Verificar el JWT
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .withIssuer(ISSUER)
                    .build();

            DecodedJWT jwt = verifier.verify(token);

            String usuario = jwt.getSubject(); // Obtener el usuario del JWT

            // Validar que el nombre en la solicitud coincida con el usuario en el token
            if (!usuario.equals(nombre)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("El nombre del parámetro no coincide con el usuario del token");
            }

            // Si todo es válido, retornar el saludo
            System.out.println(HttpStatus.OK);
            return ResponseEntity.ok("Hola " + nombre);
        }catch (JWTVerificationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido o expirado");
        }
    }
}
