package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.EstadoMovimiento;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link EstadoMovimiento}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface EstadoMovimientoRepositoryPort {

    Optional<EstadoMovimiento> findById(Integer id);

    Optional<EstadoMovimiento> findByCodigo(String codigo);

    List<EstadoMovimiento> findAll();

    EstadoMovimiento save(EstadoMovimiento estadoMovimiento);
}
