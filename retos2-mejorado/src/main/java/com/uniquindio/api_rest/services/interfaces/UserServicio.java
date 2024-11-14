package com.uniquindio.api_rest.services.interfaces;

import com.uniquindio.api_rest.dto.*;

import java.util.List;

public interface UserServicio {

    //servicio de registro
    int registrarse(RegistroDTO registroDTO);

    //update
    int editar(DetalleUserDTO datos);
    //read
    List<ItemUserDTO> listarTodos(int page, int size);
    DetalleUserDTO verDetalleUser(int id) throws Exception;
    //delete
    String eliminar(int id)throws Exception;

    void activarUser(int codigo);

    //Un usuario ´puede recuperar su contraseña
    void recuperarPasswd(String email)throws Exception;
}
