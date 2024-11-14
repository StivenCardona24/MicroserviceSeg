package com.uniquindio.api_rest.services.impl;

import com.uniquindio.api_rest.dto.*;
import com.uniquindio.api_rest.infra.security.model.UserDetailsImpl;
import com.uniquindio.api_rest.infra.security.services.JwtService;
import com.uniquindio.api_rest.services.interfaces.LoginServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServicioImpl implements LoginServicio{
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public TokenDTO login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.passwd())
        );

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        // Generar JWT
        String jwtToken = jwtService.generateToken(user);
        return new TokenDTO(jwtToken);
    }
}
