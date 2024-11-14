package com.uniquindio.api_rest.services.validations;

import com.uniquindio.api_rest.dto.*;
import com.uniquindio.api_rest.infra.errors.ValidacionDeIntegridadE;
import com.uniquindio.api_rest.model.User;
import com.uniquindio.api_rest.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidacionDeDuplicados {

    private  final UserRepo userRepo;

    public  void validarRegistro(RegistroDTO userDTO) {
        validarDuplicado(userDTO.cedula(), userDTO.email(), userDTO.phone(), null);
    }

    public void validarUpdate(DetalleUserDTO datos){
        User user = userRepo.getReferenceById(datos.id());
        validarDuplicado(datos.cedula(), datos.email(), datos.phone(), user);
    }

    private void validarDuplicado(String cedula, String email, String phone, User user) {
        if (estaRepetidaCedula(cedula, user)) {
            throw new ValidacionDeIntegridadE("La cédula " + cedula + " ya está en uso");
        }
        if (estaRepetidoCorreo(email, user)) {
            throw new ValidacionDeIntegridadE("El correo " + email + " ya está en uso");
        }
        if (estaRepetidoPhone(phone, user)) {
            throw new ValidacionDeIntegridadE("El teléfono " + phone + " ya está en uso");
        }
    }

    private boolean estaRepetidaCedula(String cedula, User user) {
        return user == null ? userRepo.findByCedula(cedula) != null
                : userRepo.findByCedulaAndIdNot(cedula, user.getId()).isPresent();
    }
    private boolean estaRepetidoCorreo(String email, User user) {
        return user == null ? userRepo.findByEmail(email) != null
                : userRepo.findByEmailAndIdNot(email, user.getId()).isPresent();
    }

    private boolean estaRepetidoPhone(String phone, User user) {
        return user == null ? userRepo.findByPhone(phone) != null
                : userRepo.findByPhoneAndIdNot(phone, user.getId()).isPresent();
    }
}
