package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.EstadoMovimiento;
import com.ithink.corebank.domain.ports.EstadoMovimientoRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.EstadoMovimientoJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link EstadoMovimientoRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class EstadoMovimientoRepositoryAdapter implements EstadoMovimientoRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final EstadoMovimientoJpaRepository jpaRepository;

    public EstadoMovimientoRepositoryAdapter(EstadoMovimientoJpaRepository jpaRepository) {
        logs.setLogger(EstadoMovimientoRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<EstadoMovimiento> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE ESTADO DE MOVIMIENTO POR ID " + id);
        Optional<EstadoMovimiento> estadoMovimiento = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(estadoMovimiento.orElse(null)));
        return estadoMovimiento;
    }

    @Override
    public Optional<EstadoMovimiento> findByCodigo(String codigo) {
        logs.info(Const.ML_INI + "CONSULTA DE ESTADO DE MOVIMIENTO POR CODIGO " + codigo);
        Optional<EstadoMovimiento> estadoMovimiento = jpaRepository.findByCodigo(codigo);
        logs.info(Const.ML_FIN + Util.toJson(estadoMovimiento.orElse(null)));
        return estadoMovimiento;
    }

    @Override
    public List<EstadoMovimiento> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS ESTADOS DE MOVIMIENTO");
        List<EstadoMovimiento> estados = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(estados));
        return estados;
    }

    @Override
    public EstadoMovimiento save(EstadoMovimiento estadoMovimiento) {
        logs.info(Const.ML_INI + Util.toJson(estadoMovimiento));
        EstadoMovimiento guardado = jpaRepository.save(estadoMovimiento);
        logs.info(Const.ML_FIN + Util.toJson(guardado));
        return guardado;
    }
}
