package com.uniquindio.api_rest.controller;


import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<MensajeDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO){
        // Generar JWT
        TokenDTO tokenDTO = loginServicio.login(loginDTO);
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
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se ha enviado el correo con el link de recuperación."));
    }

    @PostMapping("/cambiar-passwd")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@RequestBody NuevaPasswordDTO nuevaPasswordDTO) {
        System.out.println("hola hola hola");
        try {
            cuentaServicio.cambiarPasswd(nuevaPasswordDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña actualizada con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(true, e.getMessage()));
        }
    }
}
