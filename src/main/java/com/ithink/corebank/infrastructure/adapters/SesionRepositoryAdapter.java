package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Sesion;
import com.ithink.corebank.domain.ports.SesionRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.SesionJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que implementa {@link SesionRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class SesionRepositoryAdapter implements SesionRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final SesionJpaRepository jpaRepository;

    public SesionRepositoryAdapter(SesionJpaRepository jpaRepository) {
        logs.setLogger(SesionRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Sesion> findById(UUID id) {
        logs.info(Const.ML_INI + "CONSULTA DE SESION POR ID " + id);
        Optional<Sesion> sesion = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(sesion.orElse(null)));
        return sesion;
    }

    @Override
    public List<Sesion> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE SESIONES ENTRE " + fechaInicio + " Y " + fechaFin);
        List<Sesion> sesiones = jpaRepository.findBetweenDates(fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + Util.toJson(sesiones));
        return sesiones;
    }

    @Override
    public Sesion save(Sesion sesion) {
        logs.info(Const.ML_INI + Util.toJson(sesion));
        Sesion sesionGuardada = jpaRepository.save(sesion);
        logs.info(Const.ML_FIN + Util.toJson(sesionGuardada));
        return sesionGuardada;
    }
}
