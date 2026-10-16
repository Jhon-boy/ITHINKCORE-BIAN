package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Modulo;
import com.ithink.corebank.domain.ports.ModuloRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.ModuloJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link ModuloRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class ModuloRepositoryAdapter implements ModuloRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final ModuloJpaRepository jpaRepository;

    public ModuloRepositoryAdapter(ModuloJpaRepository jpaRepository) {
        logs.setLogger(ModuloRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Modulo> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE MODULO POR ID " + id);
        Optional<Modulo> modulo = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(modulo.orElse(null)));
        return modulo;
    }

    @Override
    public List<Modulo> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS MODULOS");
        List<Modulo> modulos = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(modulos));
        return modulos;
    }

    @Override
    public Modulo save(Modulo modulo) {
        logs.info(Const.ML_INI + Util.toJson(modulo));
        Modulo moduloGuardado = jpaRepository.save(modulo);
        logs.info(Const.ML_FIN + Util.toJson(moduloGuardado));
        return moduloGuardado;
    }
}
