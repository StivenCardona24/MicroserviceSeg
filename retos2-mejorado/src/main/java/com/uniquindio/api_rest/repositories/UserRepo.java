package com.uniquindio.api_rest.repositories;

import com.uniquindio.api_rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmail(String nombre);

    User findByPhone(String phone);

    User findByCedula(String cedula);

    Optional<User> findByEmailAndIdNot(String correo, Integer id);

    Optional<User> findByCedulaAndIdNot(String cedula, Integer id);

    Optional<User> findByPhoneAndIdNot(String phone, Integer id);
}
