package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Dispositivo;
import com.ithink.corebank.domain.ports.DispositivoRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.DispositivoJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa el puerto {@link DispositivoRepositoryPort}
 * delegando la persistencia en {@link DispositivoJpaRepository}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class DispositivoRepositoryAdapter implements DispositivoRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final DispositivoJpaRepository jpaRepository;

    public DispositivoRepositoryAdapter(DispositivoJpaRepository jpaRepository) {
        logs.setLogger(DispositivoRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Dispositivo> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE DISPOSITIVO POR ID " + id);
        Optional<Dispositivo> dispositivo = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(dispositivo.orElse(null)));
        return dispositivo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Dispositivo> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS DISPOSITIVOS");
        List<Dispositivo> dispositivos = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(dispositivos));
        return dispositivos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dispositivo save(Dispositivo dispositivo) {
        logs.info(Const.ML_INI + Util.toJson(dispositivo));
        Dispositivo dispositivoGuardado = jpaRepository.save(dispositivo);
        logs.info(Const.ML_FIN + Util.toJson(dispositivoGuardado));
        return dispositivoGuardado;
    }
}
