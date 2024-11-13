package com.uniquindio.api_rest.model;

import com.uniquindio.api_rest.dto.*;
import com.uniquindio.api_rest.model.enums.EstadoUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;





@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Cuenta{

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 200)
    private String lastname;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(nullable = false, length = 10, unique = true)
    private String cedula;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, length=40)
    private String address;

    // Optional user field
    @Transient
    private transient RelatedUser user;

    public User(RegistroDTO registroDTO, String passwd) {
        //datos de la cuenta
        this.setEmail(registroDTO.email());
        this.setPassword(passwd);

        //datos del usuario
        this.setName(registroDTO.name());
        this.setLastname(registroDTO.lastname());
        this.setCedula(registroDTO.cedula());
        this.setPhone(registroDTO.phone());
        this.setAddress(registroDTO.address());
        //photo
        this.setBirthdate(registroDTO.birthdate());

    }

    public void actualizar(DetalleUserDTO datos) {
        //Datos de la Cuenta
        this.setEmail( datos.email() );
        //Datos del Usuario
        this.setName( datos.name() );
       //cedula
        this.setPhone( datos.phone() );
        this.setAddress( datos.address() );
        //photo
        this.setBirthdate( datos.birthdate() );
    }
    public static class RelatedUser {
        private String id;
        private String name;
        private String email;
    }

    public void inactivar() {
        this.setEstado(EstadoUsuario.INACTIVO);
    }

    public void activar() {
        this.setEstado(EstadoUsuario.ACTIVO);
    }
}
