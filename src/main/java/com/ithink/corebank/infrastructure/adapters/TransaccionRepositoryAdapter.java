package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Transaccion;
import com.ithink.corebank.domain.ports.TransaccionRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.TransaccionJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link TransaccionRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class TransaccionRepositoryAdapter implements TransaccionRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final TransaccionJpaRepository jpaRepository;

    public TransaccionRepositoryAdapter(TransaccionJpaRepository jpaRepository) {
        logs.setLogger(TransaccionRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Transaccion> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE TRANSACCION POR ID " + id);
        Optional<Transaccion> transaccion = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(transaccion.orElse(null)));
        return transaccion;
    }

    @Override
    public List<Transaccion> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE TRANSACCIONES ENTRE " + fechaInicio + " Y " + fechaFin);
        List<Transaccion> transacciones = jpaRepository.findBetweenDates(fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + Util.toJson(transacciones));
        return transacciones;
    }

    @Override
    public Transaccion save(Transaccion transaccion) {
        logs.info(Const.ML_INI + Util.toJson(transaccion));
        Transaccion transaccionGuardada = jpaRepository.save(transaccion);
        logs.info(Const.ML_FIN + Util.toJson(transaccionGuardada));
        return transaccionGuardada;
    }
}
