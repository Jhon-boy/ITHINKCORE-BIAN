package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.SaldoHistorico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link SaldoHistorico}.
 * <p>
 * Al ser una entidad de alta concurrencia, su consulta de obtencion recibe
 * siempre un rango de fechas.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface SaldoHistoricoRepositoryPort {

    Optional<SaldoHistorico> findById(Integer id);

    List<SaldoHistorico> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    SaldoHistorico save(SaldoHistorico saldoHistorico);
}
