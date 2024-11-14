package com.uniquindio.api_rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginDTO(
        @NotBlank(message = "El campo email es obligatorio")
        @Email(message = "Ingrese una dirección de correo válida")
        @Length(max = 80, message = "El correo debe tener máximo 80 caracteres")
        String email,
        @NotBlank(message = "El campo contraseña es obligatorio")
        @Length(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
        String passwd
) {
}
