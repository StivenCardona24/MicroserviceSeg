package com.uniquindio.api_rest.services.impl;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.api_rest.dto.NotificationDTO;
import com.uniquindio.api_rest.services.interfaces.NotificationServicio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServicioImlp implements NotificationServicio {

    private final RabbitTemplate rabbitTemplate;

    private final Queue notificationQueue;

    @Override
    public void sendNotification(NotificationDTO notificationDTO) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(notificationDTO);
            rabbitTemplate.convertAndSend(notificationQueue.getName(), json);
        } catch (Exception e) {
            throw new Exception("Error al convertir el objeto a JSON") {
            };
        }
    }

}
