package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.TipoOperacion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link TipoOperacion}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface TipoOperacionRepositoryPort {

    Optional<TipoOperacion> findById(Integer id);

    List<TipoOperacion> findAll();

    TipoOperacion save(TipoOperacion tipoOperacion);
}
