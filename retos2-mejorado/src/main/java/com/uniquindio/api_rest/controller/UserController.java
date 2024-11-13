package com.uniquindio.api_rest.controller;

import com.uniquindio.api_rest.dto.DetalleUserDTO;
import com.uniquindio.api_rest.dto.ItemUserDTO;
import com.uniquindio.api_rest.dto.MensajeDTO;
import com.uniquindio.api_rest.services.interfaces.UserServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserServicio userServicio;

    @PutMapping("/editar")
    public ResponseEntity<MensajeDTO<String>> editar(@Valid @RequestBody DetalleUserDTO userDTO) throws Exception{
        userServicio.editar(userDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario actualizado correctamente"));
    }

    @GetMapping("/detalle/{codigo}")
    public ResponseEntity<MensajeDTO<DetalleUserDTO>> verDetallePaciente(@PathVariable int codigo) throws Exception{
        System.out.println("Código recibido: " + codigo);
        return ResponseEntity.ok(new MensajeDTO<>(false, userServicio.verDetalleUser(codigo)));
    }

    @GetMapping("/listar-todos")
    public ResponseEntity<MensajeDTO<List<ItemUserDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new MensajeDTO<>(false, userServicio.listarTodos(page, size)));
    }
    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable int codigo) throws Exception{
        userServicio.eliminar(codigo);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario eliminado correctamente"));
    }

    @PutMapping("/activar/{codigo}")
    public ResponseEntity<MensajeDTO<String>> activarUser(@PathVariable int codigo){
        userServicio.activarUser(codigo);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario activado"));
    }
}
