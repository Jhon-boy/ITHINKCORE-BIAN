package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Reporte;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Reporte}.
 * <p>
 * Al ser una entidad de alta concurrencia, su consulta de obtencion recibe
 * siempre un rango de fechas.
 * </p>
 *
 * @author Jhon
 * @version 1.0
 */
public interface ReporteRepositoryPort {

    Optional<Reporte> findById(Integer id);

    List<Reporte> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Reporte save(Reporte reporte);
}
