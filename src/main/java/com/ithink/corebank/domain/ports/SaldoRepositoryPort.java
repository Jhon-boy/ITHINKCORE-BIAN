package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Saldo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Saldo}.
 * <p>
 * Al ser una entidad de alta concurrencia, su consulta de obtencion recibe
 * siempre un rango de fechas.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface SaldoRepositoryPort {

    Optional<Saldo> findById(Integer id);

    Optional<Saldo> findByIdCuenta(Integer idCuenta);

    List<Saldo> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Saldo save(Saldo saldo);
}
