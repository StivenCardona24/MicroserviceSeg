package com.uniquindio.api_rest.services.impl;

import com.uniquindio.api_rest.dto.LogDTO;
import com.uniquindio.api_rest.services.interfaces.LogServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class LogServicioImpl implements LogServicio {

    private final RabbitTemplate rabbitTemplate;
    private final Queue logQueue; // Aquí defines la cola a la que se enviarán los logs

    @Override
    public void registrarLog(LogDTO logDTO) throws JsonProcessingException {
        try{
        // Convertir el LogDTO a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(logDTO);
        // Convertir el LogDTO a un formato adecuado y enviar a la cola
        rabbitTemplate.convertAndSend(logQueue.getName(), json);
        }
        catch (JsonProcessingException e){
            throw new JsonProcessingException("Error al convertir el objeto a JSON"){};
        }
    }
}
