package com.uniquindio.api_rest.controller;


import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uniquindio.api_rest.dto.LogDTO;
import com.uniquindio.api_rest.dto.LoginDTO;
import com.uniquindio.api_rest.dto.MensajeDTO;
import com.uniquindio.api_rest.dto.NuevaPasswordDTO;
import com.uniquindio.api_rest.dto.RegistroDTO;
import com.uniquindio.api_rest.dto.TokenDTO;
import com.uniquindio.api_rest.services.interfaces.CuentaServicio;
import com.uniquindio.api_rest.services.interfaces.LogServicio;
import com.uniquindio.api_rest.services.interfaces.LoginServicio;
import com.uniquindio.api_rest.services.interfaces.UserServicio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Login", description = "operacion pertienente para login")
public class LoginController {

    private final LoginServicio loginServicio;
    private final UserServicio userServicio;
    private final CuentaServicio cuentaServicio;
    private final LogServicio logServicio;
    @Operation(summary = "Función para logearse", description = "una descripción más amplia")
    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO) throws JsonProcessingException{
        // Generar JWT
        TokenDTO tokenDTO = loginServicio.login(loginDTO);
        // Log de la acción de login
        logServicio.registrarLog(new LogDTO(
            "LoginService",
            "INFO",
            "login",
            new Date(),
            "Intento de inicio de sesión",
            "Inicio de sesión para el usuario con email " + loginDTO.email()
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, tokenDTO));
    }

    @PostMapping("/registrarse")
    public ResponseEntity<MensajeDTO<String>> registrarse(@Valid @RequestBody RegistroDTO pacienteDTO) throws Exception{
        int codigo = userServicio.registrarse(pacienteDTO);
        logServicio.registrarLog(new LogDTO(
            "UserService",
            "INFO",
            "register",
            new Date(),
            "Usuario registrado",
            "El usuario con ID " + codigo + " fue registrado correctamente"
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario "+codigo+" registrado correctamente"));
    }
    @GetMapping("/recuperar-passwd/{email}")
    public ResponseEntity<MensajeDTO<String>> recuperarPasswd(@Valid @PathVariable String email) throws Exception {
        userServicio.recuperarPasswd(email);
         // Log de recuperación de contraseña
        logServicio.registrarLog(new LogDTO(
            "UserService",
            "INFO",
            "recoverPassword",
            new Date(),
            "Recuperación de contraseña",
            "Solicitud de recuperación de contraseña para el usuario con email " + email
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se ha enviado el correo con el link de recuperación."));
    }

    @PostMapping("/cambiar-passwd")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@RequestBody NuevaPasswordDTO nuevaPasswordDTO) throws JsonProcessingException{
        System.out.println("hola hola hola");
        try {
            cuentaServicio.cambiarPasswd(nuevaPasswordDTO);
            // Log de cambio de contraseña
            logServicio.registrarLog(new LogDTO(
                "AccountService",
                "INFO",
                "changePassword",
                new Date(),
                "Cambio de contraseña",
                "La contraseña para el usuario con ID " + nuevaPasswordDTO.codigoCuenta() + " fue cambiada exitosamente"
            ));
            return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña actualizada con éxito."));
        } catch (Exception e) {
            // Log de error al cambiar la contraseña
            logServicio.registrarLog(new LogDTO(
                "AccountService",
                "ERROR",
                "changePassword",
                new Date(),
                "Error al cambiar contraseña",
                "Error al cambiar contraseña para el usuario con ID " + nuevaPasswordDTO.codigoCuenta() + ": " + e.getMessage()
            ));
            return ResponseEntity.badRequest().body(new MensajeDTO<>(true, e.getMessage()));
        }
    }
}
