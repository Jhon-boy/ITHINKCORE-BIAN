package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Transaccion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Transaccion}.
 * <p>
 * Al ser una entidad de alta concurrencia, su consulta de obtencion recibe
 * siempre un rango de fechas.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface TransaccionRepositoryPort {

    Optional<Transaccion> findById(Integer id);

    List<Transaccion> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Transaccion save(Transaccion transaccion);
}
