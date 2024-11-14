package com.uniquindio.api_rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String recipient;     // Correo del destinatario
    private String subject;            // Asunto del mensaje
    private String message;           
    private String channel;  
}
