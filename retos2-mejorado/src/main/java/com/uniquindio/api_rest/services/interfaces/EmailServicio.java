package com.uniquindio.api_rest.services.interfaces;

import com.uniquindio.api_rest.dto.EmailDTO;

public interface EmailServicio {
    void enviarEmail(EmailDTO emailDTO) throws Exception;
}
