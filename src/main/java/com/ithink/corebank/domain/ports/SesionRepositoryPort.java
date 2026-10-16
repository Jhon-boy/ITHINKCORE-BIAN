package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Sesion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de acceso a datos para la entidad {@link Sesion}.
 * <p>
 * Al ser una entidad de alta concurrencia, su consulta de obtencion recibe
 * siempre un rango de fechas.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface SesionRepositoryPort {

    Optional<Sesion> findById(UUID id);

    List<Sesion> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Sesion save(Sesion sesion);
}
