package com.uniquindio.api_rest.services.impl;

import com.uniquindio.api_rest.dto.*;
import com.uniquindio.api_rest.infra.errors.ValidacionDeIntegridadE;
import com.uniquindio.api_rest.model.User;
import com.uniquindio.api_rest.model.enums.EstadoUsuario;
import com.uniquindio.api_rest.repositories.UserRepo;
import com.uniquindio.api_rest.services.interfaces.*;
import com.uniquindio.api_rest.services.validations.ValidacionDeDuplicados;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServicioImpl implements UserServicio {

    private  final UserRepo userRepo;
    private final CuentaServicioImpl cuentaServicios;
    private final ValidacionDeDuplicados validacion;
    private  final PasswordEncoder passwordEncoder;
    @Override
    public int registrarse(RegistroDTO registroDTO) {
        validacion.validarRegistro(registroDTO);
        String passwd = passwordEncoder.encode(registroDTO.password());
        User user = userRepo.save(new User(registroDTO, passwd));
        return user.getId();
    }

    @Override
    public int editar(DetalleUserDTO datos) {
        User user = validar(datos);
        validacion.validarUpdate(datos);
        user.actualizar(datos);
        return user.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemUserDTO> listarTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepo.findAll(pageable);
        if (usersPage.isEmpty()) {
            throw new ValidationException("No hay usuarios registrados");
        }
        //Hacemos un mapeo de cada uno de los objetos de tipo Paciente a objetos de tipo ItemPacienteDTO
        return usersPage.getContent().stream()
                .filter(u -> u.getEstado() == EstadoUsuario.ACTIVO)
                .map(ItemUserDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleUserDTO verDetalleUser(int id) {

        User user = obtenerUsuarioPorId(id);
        if(user.getEstado()==EstadoUsuario.INACTIVO){
            throw new ValidationException("El usurio está eliminado");
        }
        //Hacemos un mapeo de un objeto de tipo Paciente a un objeto de tipo DetallePacienteDTO
        return new DetalleUserDTO(user);
    }

    @Override
    public String eliminar(int id) throws Exception {
        User user = obtenerUsuarioPorId(id);
        user.inactivar();
        return "Usuario eliminado con éxito";
    }

    @Override
    public void recuperarPasswd(String email) throws Exception {
        cuentaServicios.enviarLinkRecuperacion(email);
    }

    @Override
    public void activarUser(int codigo){
        User user = obtenerUsuarioPorId(codigo);
        if(user.getEstado()==EstadoUsuario.ACTIVO){
            throw new ValidationException("El paciente ya esta activo");
        }
        user.activar();
    }

    private User validar(DetalleUserDTO datos) {
        Optional<User> opcional = userRepo.findById(datos.id());
        if( opcional.isEmpty() ){
            throw new ValidacionDeIntegridadE("No existe un usuario con el código "+datos.id());
        }
        return opcional.get();
    }

    private User obtenerUsuarioPorId(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ValidacionDeIntegridadE("No existe un usuario con el código " + id));
    }
}
