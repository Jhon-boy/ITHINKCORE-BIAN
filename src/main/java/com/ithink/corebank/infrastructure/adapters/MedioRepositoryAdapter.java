package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Medio;
import com.ithink.corebank.domain.ports.MedioRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.MedioJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link MedioRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class MedioRepositoryAdapter implements MedioRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final MedioJpaRepository jpaRepository;

    public MedioRepositoryAdapter(MedioJpaRepository jpaRepository) {
        logs.setLogger(MedioRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Medio> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE MEDIO POR ID " + id);
        Optional<Medio> medio = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(medio.orElse(null)));
        return medio;
    }

    @Override
    public List<Medio> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS MEDIOS");
        List<Medio> medios = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(medios));
        return medios;
    }

    @Override
    public Medio save(Medio medio) {
        logs.info(Const.ML_INI + Util.toJson(medio));
        Medio medioGuardado = jpaRepository.save(medio);
        logs.info(Const.ML_FIN + Util.toJson(medioGuardado));
        return medioGuardado;
    }
}
