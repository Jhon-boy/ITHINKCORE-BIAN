package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.RolUsuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link RolUsuario}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface RolUsuarioRepositoryPort {

    Optional<RolUsuario> findById(Integer id);

    List<RolUsuario> findAll();

    RolUsuario save(RolUsuario rolUsuario);
}
