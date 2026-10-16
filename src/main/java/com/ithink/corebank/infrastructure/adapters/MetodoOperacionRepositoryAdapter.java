package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.MetodoOperacion;
import com.ithink.corebank.domain.ports.MetodoOperacionRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.MetodoOperacionJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link MetodoOperacionRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class MetodoOperacionRepositoryAdapter implements MetodoOperacionRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final MetodoOperacionJpaRepository jpaRepository;

    public MetodoOperacionRepositoryAdapter(MetodoOperacionJpaRepository jpaRepository) {
        logs.setLogger(MetodoOperacionRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<MetodoOperacion> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE METODO DE OPERACION POR ID " + id);
        Optional<MetodoOperacion> metodo = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(metodo.orElse(null)));
        return metodo;
    }

    @Override
    public List<MetodoOperacion> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS METODOS DE OPERACION");
        List<MetodoOperacion> metodos = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(metodos));
        return metodos;
    }

    @Override
    public MetodoOperacion save(MetodoOperacion metodoOperacion) {
        logs.info(Const.ML_INI + Util.toJson(metodoOperacion));
        MetodoOperacion metodoGuardado = jpaRepository.save(metodoOperacion);
        logs.info(Const.ML_FIN + Util.toJson(metodoGuardado));
        return metodoGuardado;
    }
}
