package com.uniquindio.api_rest.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDate;

public record RegistroDTO(
        @NotBlank
        @Length(max = 100, message = "El nombre debe tener máximo 100 caracteres")
        String name,

        @NotBlank
        @Length(max = 100, message = "El apellido debe tener máximo 100 caracteres")
        String lastname,

        @NotBlank
        @Length(max = 10, message = "La cédula debe tener máximo 10 caracteres")
        String cedula,

        @NotBlank
        @Email(message = "Ingrese una dirección de correo válida")
        @Length(max = 80, message = "El correo debe tener máximo 80 caracteres")
        String email,

        @NotBlank
        @Length(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
        String password,

        @NotBlank
        @Pattern(regexp = "\\+\\d{10,12}", message = "El teléfono debe comenzar con '+' y contener entre 10 y 12 dígitos numéricos")
        String phone,

        @NotNull
        @Past(message = "Seleccione una fecha de nacimiento válida")
        LocalDate birthdate,

        @NotBlank
        @Length(max = 200, message = "La dirección debe tener máximo 200 caracteres")
        String address
) {
}
