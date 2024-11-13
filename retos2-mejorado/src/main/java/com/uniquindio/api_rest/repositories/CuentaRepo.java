package com.uniquindio.api_rest.repositories;

import com.uniquindio.api_rest.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends JpaRepository<Cuenta, Integer> {
    Optional<Cuenta> findByEmail(String username);
}
