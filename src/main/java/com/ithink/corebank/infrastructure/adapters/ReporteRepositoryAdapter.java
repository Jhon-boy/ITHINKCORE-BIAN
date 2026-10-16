package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Reporte;
import com.ithink.corebank.domain.ports.ReporteRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.ReporteJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link ReporteRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class ReporteRepositoryAdapter implements ReporteRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final ReporteJpaRepository jpaRepository;

    public ReporteRepositoryAdapter(ReporteJpaRepository jpaRepository) {
        logs.setLogger(ReporteRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Reporte> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE REPORTE POR ID " + id);
        Optional<Reporte> reporte = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(reporte.orElse(null)));
        return reporte;
    }

    @Override
    public List<Reporte> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE REPORTES ENTRE " + fechaInicio + " Y " + fechaFin);
        List<Reporte> reportes = jpaRepository.findBetweenDates(fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + Util.toJson(reportes));
        return reportes;
    }

    @Override
    public Reporte save(Reporte reporte) {
        logs.info(Const.ML_INI + Util.toJson(reporte));
        Reporte reporteGuardado = jpaRepository.save(reporte);
        logs.info(Const.ML_FIN + Util.toJson(reporteGuardado));
        return reporteGuardado;
    }
}
