package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.TipoOperacion;
import com.ithink.corebank.domain.ports.TipoOperacionRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.TipoOperacionJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link TipoOperacionRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class TipoOperacionRepositoryAdapter implements TipoOperacionRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final TipoOperacionJpaRepository jpaRepository;

    public TipoOperacionRepositoryAdapter(TipoOperacionJpaRepository jpaRepository) {
        logs.setLogger(TipoOperacionRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TipoOperacion> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE TIPO DE OPERACION POR ID " + id);
        Optional<TipoOperacion> tipoOperacion = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(tipoOperacion.orElse(null)));
        return tipoOperacion;
    }

    @Override
    public List<TipoOperacion> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS TIPOS DE OPERACION");
        List<TipoOperacion> tipos = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(tipos));
        return tipos;
    }

    @Override
    public TipoOperacion save(TipoOperacion tipoOperacion) {
        logs.info(Const.ML_INI + Util.toJson(tipoOperacion));
        TipoOperacion tipoGuardado = jpaRepository.save(tipoOperacion);
        logs.info(Const.ML_FIN + Util.toJson(tipoGuardado));
        return tipoGuardado;
    }
}
