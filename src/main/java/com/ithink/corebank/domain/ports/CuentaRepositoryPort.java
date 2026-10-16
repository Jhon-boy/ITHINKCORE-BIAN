package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Cuenta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Cuenta}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface CuentaRepositoryPort {

    Optional<Cuenta> findById(Integer id);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    List<Cuenta> findAll();

    Cuenta save(Cuenta cuenta);
}
