package com.uniquindio.api_rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uniquindio.api_rest.dto.DetalleUserDTO;
import com.uniquindio.api_rest.dto.ItemUserDTO;
import com.uniquindio.api_rest.dto.LogDTO;
import com.uniquindio.api_rest.dto.MensajeDTO;
import com.uniquindio.api_rest.services.interfaces.UserServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uniquindio.api_rest.services.interfaces.LogServicio;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserServicio userServicio;
    private final LogServicio logServicio;

    @PutMapping("/editar")
    public ResponseEntity<MensajeDTO<String>> editar(@Valid @RequestBody DetalleUserDTO userDTO) throws Exception{
        userServicio.editar(userDTO);
        // Log de edición de usuario
        logServicio.registrarLog(new LogDTO(
            "UserService",
            "INFO",
            "editUser",
            new Date(),
            "Edición de usuario",
            "El usuario con ID " + userDTO.id() + " fue editado correctamente"
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario actualizado correctamente"));
    }

    @GetMapping("/detalle/{codigo}")
    public ResponseEntity<MensajeDTO<DetalleUserDTO>> verDetallePaciente(@PathVariable int codigo) throws Exception{
        System.out.println("Código recibido: " + codigo);
        logServicio.registrarLog(new LogDTO(
            "UserService",
            "INFO",
            "viewUserDetail",
            new Date(),
            "Detalle de usuario",
            "Se visualizó el detalle del usuario con ID " + codigo
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, userServicio.verDetalleUser(codigo)));
    }

    @GetMapping("/listar-todos")
    public ResponseEntity<MensajeDTO<List<ItemUserDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException{
                // Log de listado de usuarios
            logServicio.registrarLog(new LogDTO(
                "UserService",
                "INFO",
                "listAllUsers",
                new Date(),
                "Listado de usuarios",
                "Se listaron todos los usuarios, página " + page + ", tamaño " + size
            ));
        return ResponseEntity.ok(new MensajeDTO<>(false, userServicio.listarTodos(page, size)));
    }
    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable int codigo) throws Exception{
        userServicio.eliminar(codigo);
        // Log de eliminación de usuario
        logServicio.registrarLog(new LogDTO(
            "UserService",
            "INFO",
            "deleteUser",
            new Date(),
            "Eliminación de usuario",
            "El usuario con ID " + codigo + " fue eliminado correctamente"
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario eliminado correctamente"));
    }

    @PutMapping("/activar/{codigo}")
    public ResponseEntity<MensajeDTO<String>> activarUser(@PathVariable int codigo) throws JsonProcessingException{
        userServicio.activarUser(codigo);
        // Log de activación de usuario
        logServicio.registrarLog(new LogDTO(
            "UserService",
            "INFO",
            "activateUser",
            new Date(),
            "Activación de usuario",
            "El usuario con ID " + codigo + " fue activado correctamente"
        ));
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario activado"));
    }
}
