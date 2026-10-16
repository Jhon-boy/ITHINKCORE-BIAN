package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Sucursal;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Sucursal}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface SucursalRepositoryPort {

    Optional<Sucursal> findById(Integer id);

    List<Sucursal> findAll();

    Sucursal save(Sucursal sucursal);
}
