package com.uniquindio.api_rest.services.impl;

import com.uniquindio.api_rest.dto.EmailDTO;
import com.uniquindio.api_rest.services.interfaces.EmailServicio;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServicioImpl implements EmailServicio {

    private final JavaMailSender javaMailSender;
    private final String emailFrom = "no_reply@dominio.com";
    @Override
    public void enviarEmail(EmailDTO emailDTO){
        try {
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje);
            helper.setSubject(emailDTO.asunto());
            helper.setText(emailDTO.cuerpo(), true);
            helper.setTo(emailDTO.destinatario());
            helper.setFrom(emailFrom);
            javaMailSender.send(mensaje);
        }catch (MessagingException e){
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
