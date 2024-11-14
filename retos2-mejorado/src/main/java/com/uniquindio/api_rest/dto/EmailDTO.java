package com.uniquindio.api_rest.dto;

public record EmailDTO(
        String destinatario,
        String asunto,
        String cuerpo
) {
}
