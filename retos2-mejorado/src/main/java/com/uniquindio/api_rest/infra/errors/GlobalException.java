package com.uniquindio.api_rest.infra.errors;

import com.uniquindio.api_rest.dto.MensajeDTO;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalException {

    // Manejo de excepciones genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDTO<String>> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true, e.getMessage()));
    }

    // Manejo de validaciones de negocio o integridad
    @ExceptionHandler({ValidacionDeIntegridadE.class, ValidationException.class})
    public ResponseEntity<MensajeDTO<String>> handleValidacionDeNegocio(Exception e){
        return buildErrorResponse(e.getMessage());
    }

    // Manejo de validaciones de campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensajeDTO<List<ValidacionDTO>>> tratarError400(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors().stream().map(ValidacionDTO::new).toList();
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true,errors));
    }

    // Manejo de excepción UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MensajeDTO<String>> handleUserNotFoundException(UsernameNotFoundException e) {
        String message = "El usuario no existe. Por favor, verifique el correooooo.";
        return buildErrorResponse(message);
    }

    // Método privado para generar respuestas de error de forma consistente
    private ResponseEntity<MensajeDTO<String>> buildErrorResponse(String message) {
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true, message));
    }

    // Record para encapsular los errores de validación
    public record ValidacionDTO(String campo, String mensaje){
        public ValidacionDTO(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
