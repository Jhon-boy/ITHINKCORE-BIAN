package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.MetodoOperacion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link MetodoOperacion}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface MetodoOperacionRepositoryPort {

    Optional<MetodoOperacion> findById(Integer id);

    List<MetodoOperacion> findAll();

    MetodoOperacion save(MetodoOperacion metodoOperacion);
}
