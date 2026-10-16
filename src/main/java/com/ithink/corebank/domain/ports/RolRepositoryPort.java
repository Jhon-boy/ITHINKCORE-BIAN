package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Rol;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Rol}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface RolRepositoryPort {

    Optional<Rol> findById(Integer id);

    List<Rol> findAll();

    Rol save(Rol rol);
}
