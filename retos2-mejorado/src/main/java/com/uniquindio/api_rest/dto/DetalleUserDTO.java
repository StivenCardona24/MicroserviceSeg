package com.uniquindio.api_rest.dto;

import com.uniquindio.api_rest.model.User;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record DetalleUserDTO(
        int id,
        @NotBlank
        @Length(max = 200, message = "El nombre debe tener máximo 200 caracteres")
        String name,
        @NotBlank
        @Length(max = 200, message = "El apellido debe tener máximo 200 caracteres")
        String lastname,

        @NotBlank
        @Length(max = 10, message = "La cédula debe tener máximo 10 caracteres")
        String cedula,
        @NotBlank
        @Length(max = 80, message = "El correo debe tener máximo 80 caracteres")
        @Email(message = "Ingrese una dirección de correo electrónico válida")
        String email,
        @NotBlank
        @Pattern(regexp = "\\+\\d{10,12}", message = "El teléfono debe comenzar con '+' y contener entre 10 y 12 dígitos numéricos")
        String phone,
        @NotNull
        @Past(message = "Seleccione una fecha de nacimiento correcta")
        LocalDate birthdate,
        @NotBlank
        String address
) {
    public DetalleUserDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getCedula(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getAddress()
        );
    }
}
