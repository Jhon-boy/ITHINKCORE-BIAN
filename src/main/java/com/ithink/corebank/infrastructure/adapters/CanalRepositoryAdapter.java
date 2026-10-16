package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Canal;
import com.ithink.corebank.domain.ports.CanalRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.CanalJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link CanalRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class CanalRepositoryAdapter implements CanalRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final CanalJpaRepository jpaRepository;

    public CanalRepositoryAdapter(CanalJpaRepository jpaRepository) {
        logs.setLogger(CanalRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Canal> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE CANAL POR ID " + id);
        Optional<Canal> canal = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(canal.orElse(null)));
        return canal;
    }

    @Override
    public List<Canal> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS CANALES");
        List<Canal> canales = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(canales));
        return canales;
    }

    @Override
    public Canal save(Canal canal) {
        logs.info(Const.ML_INI + Util.toJson(canal));
        Canal canalGuardado = jpaRepository.save(canal);
        logs.info(Const.ML_FIN + Util.toJson(canalGuardado));
        return canalGuardado;
    }
}
