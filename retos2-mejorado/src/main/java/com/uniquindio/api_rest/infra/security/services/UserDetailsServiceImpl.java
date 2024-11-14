package com.uniquindio.api_rest.infra.security.services;

import com.uniquindio.api_rest.infra.errors.UsuarioInactivoException;
import com.uniquindio.api_rest.infra.security.model.UserDetailsImpl;
import com.uniquindio.api_rest.model.Cuenta;
import com.uniquindio.api_rest.model.User;
import com.uniquindio.api_rest.model.enums.EstadoUsuario;
import com.uniquindio.api_rest.repositories.CuentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CuentaRepo cuentaRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return cuentaRepo.findByEmail(email)
                .map(cuenta -> {
                    if (cuenta instanceof User user && user.getEstado() == EstadoUsuario.INACTIVO) {
                        throw new UsuarioInactivoException("El usuario no está activo");
                    }
                    return UserDetailsImpl.build(cuenta);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con correo " + email + " no existe"));
    }
}
