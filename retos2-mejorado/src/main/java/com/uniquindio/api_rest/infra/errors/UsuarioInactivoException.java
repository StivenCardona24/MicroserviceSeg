package com.uniquindio.api_rest.infra.errors;

public class UsuarioInactivoException extends RuntimeException {
    public UsuarioInactivoException(String s) {
        super(s);
    }
}
