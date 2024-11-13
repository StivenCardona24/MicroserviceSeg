package com.uniquindio.api_rest.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uniquindio.api_rest.dto.LogDTO;

public interface LogServicio {
    void registrarLog(LogDTO logDTO) throws JsonProcessingException;
}
