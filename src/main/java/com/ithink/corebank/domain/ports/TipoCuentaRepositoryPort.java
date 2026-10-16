package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.TipoCuenta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link TipoCuenta}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface TipoCuentaRepositoryPort {

    Optional<TipoCuenta> findById(Integer id);

    List<TipoCuenta> findAll();

    TipoCuenta save(TipoCuenta tipoCuenta);
}
