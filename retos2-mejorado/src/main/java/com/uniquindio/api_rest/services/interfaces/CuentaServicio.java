package com.uniquindio.api_rest.services.interfaces;

import com.uniquindio.api_rest.dto.NuevaPasswordDTO;
import com.uniquindio.api_rest.model.Cuenta;

public interface CuentaServicio {
    void enviarLinkRecuperacion(String email) throws Exception;
    public void cambiarPasswd(NuevaPasswordDTO nuevaPasswordDTO) throws Exception;

    Cuenta obtenerCuentaCodigo(int codigoCuenta);
}
