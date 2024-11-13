package com.uniquindio.api_rest.controller;


import com.uniquindio.api_rest.dto.*;
import com.uniquindio.api_rest.services.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Login", description = "operacion pertienente para login")
public class LoginController {

    private final LoginServicio loginServicio;
    private final UserServicio userServicio;
    private final CuentaServicio cuentaServicio;
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
