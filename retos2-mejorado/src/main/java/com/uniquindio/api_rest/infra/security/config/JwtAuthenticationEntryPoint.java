package com.uniquindio.api_rest.infra.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.api_rest.dto.MensajeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        MensajeDTO<String> dto = new MensajeDTO<>(true, authException.getMessage());

        // Configuración de la respuesta
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// error 401

        //try-with-resources
        try (var writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(dto));
            writer.flush();
        }
    }
}
