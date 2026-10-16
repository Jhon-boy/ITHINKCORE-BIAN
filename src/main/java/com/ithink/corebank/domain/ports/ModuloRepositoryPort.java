package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Modulo;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Modulo}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface ModuloRepositoryPort {

    Optional<Modulo> findById(Integer id);

    List<Modulo> findAll();

    Modulo save(Modulo modulo);
}
