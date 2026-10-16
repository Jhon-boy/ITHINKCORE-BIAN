package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Movimiento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Movimiento}.
 * <p>
 * Al ser una entidad de alta concurrencia, su consulta de obtencion recibe
 * siempre un rango de fechas.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface MovimientoRepositoryPort {

    Optional<Movimiento> findById(Integer id);

    List<Movimiento> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Movimientos de una cuenta creados dentro del rango.
     *
     * @param idCuenta identificador de cuenta
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return movimientos del rango
     */
    List<Movimiento> findByCuentaBetween(Integer idCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Movimiento save(Movimiento movimiento);
}
