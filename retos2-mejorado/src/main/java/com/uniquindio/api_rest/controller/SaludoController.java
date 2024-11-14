package com.uniquindio.api_rest.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.uniquindio.api_rest.dto.LogDTO;
import com.uniquindio.api_rest.services.interfaces.LogServicio;

import lombok.RequiredArgsConstructor;

import java.util.Date;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saludo")
public class SaludoController {

    private static final String SECRET_KEY = "mysecretkey"; // Clave secreta utilizada para firmar y verificar el JWT
    private static final String ISSUER = "ingesis.uniquindio.edu.co";
    private final LogServicio logServicio;

    @GetMapping
    public ResponseEntity<String> saludo(
            @RequestParam(required = false) String nombre,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws JsonProcessingException{

        if (nombre==null || nombre.isEmpty()){
            //Solicitud inválida: Si el nombre no está presente, se registra un log de advertencia (WARN).
            logServicio.registrarLog(new LogDTO(
                "SaludoService",
                "WARN",
                "saludo",
                new Date(),
                "Solicitud inválida",
                "Solicitud sin nombre en el parámetro"
            ));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solicitud no válida: El nombre es obligatorio");
        }

        // Verificar la cabecera Authorization
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            //Autenticación fallida: Si no hay token o el formato es incorrecto, se registra un log de advertencia.
            logServicio.registrarLog(new LogDTO(
                "SaludoService",
                "WARN",
                "saludo",
                new Date(),
                "Autenticación fallida",
                "JWT no proporcionado o formato incorrecto"
            ));
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
                //Nombre no coincide: Si el nombre proporcionado no coincide con el usuario del token, se registra un log de advertencia.
                logServicio.registrarLog(new LogDTO(
                    "SaludoService",
                    "WARN",
                    "saludo",
                    new Date(),
                    "Nombre no coincide",
                    "El nombre proporcionado no coincide con el usuario del token"
                ));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("El nombre del parámetro no coincide con el usuario del token");
            }


            //Saludo exitoso: Si todo es válido, se registra un log informativo (INFO) para documentar el saludo exitoso.
            logServicio.registrarLog(new LogDTO(
                "SaludoService",
                "INFO",
                "saludo",
                new Date(),
                "Saludo exitoso",
                "Saludo exitoso para el usuario: " + nombre
            ));

            // Si todo es válido, retornar el saludo
            System.out.println(HttpStatus.OK);
            return ResponseEntity.ok("Hola " + nombre);
        }catch (JWTVerificationException ex) {
            //Token inválido: Si el token es inválido o expirado, se registra un log de error (ERROR).
            logServicio.registrarLog(new LogDTO(
                "SaludoService",
                "ERROR",
                "saludo",
                new Date(),
                "Token inválido",
                "El token proporcionado es inválido o está expirado"
            ));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido o expirado");
        }
    }
}
