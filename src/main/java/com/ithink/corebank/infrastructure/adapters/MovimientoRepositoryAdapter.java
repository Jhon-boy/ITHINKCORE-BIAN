package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Movimiento;
import com.ithink.corebank.domain.ports.MovimientoRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.MovimientoJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link MovimientoRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final MovimientoJpaRepository jpaRepository;

    public MovimientoRepositoryAdapter(MovimientoJpaRepository jpaRepository) {
        logs.setLogger(MovimientoRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Movimiento> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE MOVIMIENTO POR ID " + id);
        Optional<Movimiento> movimiento = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(movimiento.orElse(null)));
        return movimiento;
    }

    @Override
    public List<Movimiento> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE MOVIMIENTOS ENTRE " + fechaInicio + " Y " + fechaFin);
        List<Movimiento> movimientos = jpaRepository.findBetweenDates(fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + Util.toJson(movimientos));
        return movimientos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movimiento> findByCuentaBetween(Integer idCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE MOVIMIENTOS CUENTA " + idCuenta
                + " ENTRE " + fechaInicio + " Y " + fechaFin);
        List<Movimiento> movimientos = jpaRepository.findByCuentaBetween(idCuenta, fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + "count=" + movimientos.size());
        return movimientos;
    }

    @Override
    public Movimiento save(Movimiento movimiento) {
        logs.info(Const.ML_INI + Util.toJson(movimiento));
        Movimiento movimientoGuardado = jpaRepository.save(movimiento);
        logs.info(Const.ML_FIN + Util.toJson(movimientoGuardado));
        return movimientoGuardado;
    }
}
