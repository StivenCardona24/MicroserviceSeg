package com.uniquindio.api_rest.dto;

public record NuevaPasswordDTO(
        int codigoCuenta,

        String token,
        String nuevaPasswd
) {
}
