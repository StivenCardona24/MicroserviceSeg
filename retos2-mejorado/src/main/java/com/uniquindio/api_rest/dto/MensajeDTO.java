package com.uniquindio.api_rest.dto;

public record MensajeDTO<T>(
        Boolean valor,
        T respuesta
) {
}
