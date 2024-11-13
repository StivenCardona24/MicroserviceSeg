package com.uniquindio.api_rest.services.impl;

import com.uniquindio.api_rest.dto.*;
import com.uniquindio.api_rest.model.Cuenta;
import com.uniquindio.api_rest.repositories.CuentaRepo;
import com.uniquindio.api_rest.services.interfaces.*;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CuentaServicioImpl implements CuentaServicio {

    private final CuentaRepo cuentaRepo;
    private final EmailServicioImpl emailServicio;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.recovery-link-base-url}")
    private String recoveryBaseUrl;
    @Override
    public void enviarLinkRecuperacion(String email) throws Exception {
        Cuenta cuenta = cuentaRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("No existe una cuenta con el correo " + email));

        String tokenRecuperacion = generarTokenRecuperacion(cuenta.getId());
        String recoveryLink = recoveryBaseUrl + "/cambiar-passwd/" + tokenRecuperacion;

        emailServicio.enviarEmail(new EmailDTO(
                cuenta.getEmail(),
                "Recuperación de contraseña",
                "Hola, para recuperar tu contraseña ingresa al siguiente link: " + recoveryLink
        ));

    }

    @Override
    public void cambiarPasswd(NuevaPasswordDTO nuevaPasswordDTO) throws Exception {
        String tokenRecuperacion = decodificarToken(nuevaPasswordDTO.token());
        String[] datos = tokenRecuperacion.split(";");
        int codigoCuenta = Integer.parseInt(datos[0]);
        LocalDateTime fechaGeneracionToken = LocalDateTime.parse(datos[1]);

        validarExpiracionToken(fechaGeneracionToken);

        // Obtener la cuenta correspondiente
        Cuenta cuenta = obtenerCuentaCodigo(codigoCuenta);
        // Encriptar la nueva contraseña antes de guardarla
        cuenta.setPassword(passwordEncoder.encode(nuevaPasswordDTO.nuevaPasswd()));
    }

    private String generarTokenRecuperacion(int cuentaId) {
        LocalDateTime fecha = LocalDateTime.now();
        return Base64.getEncoder().encodeToString((cuentaId + ";" + fecha).getBytes());
    }

    private String decodificarToken(String token) {
        return new String(Base64.getDecoder().decode(token));
    }

    private void validarExpiracionToken(LocalDateTime fechaGeneracionToken) throws Exception {
        if (Duration.between(fechaGeneracionToken, LocalDateTime.now()).toMinutes() > 30) {
            throw new Exception("El link de recuperación ha expirado");
        }
    }

    @Override
    public Cuenta obtenerCuentaCodigo(int codigoCuenta) {
        return cuentaRepo.findById(codigoCuenta)
                .orElseThrow(() -> new ValidationException("No existe una cuenta con el código " + codigoCuenta));
    }
}
