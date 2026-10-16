package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la tabla {@code TCUENTA}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface CuentaJpaRepository extends JpaRepository<Cuenta, Integer> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
