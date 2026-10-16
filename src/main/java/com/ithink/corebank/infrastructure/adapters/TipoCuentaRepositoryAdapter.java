package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.TipoCuenta;
import com.ithink.corebank.domain.ports.TipoCuentaRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.TipoCuentaJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link TipoCuentaRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class TipoCuentaRepositoryAdapter implements TipoCuentaRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final TipoCuentaJpaRepository jpaRepository;

    public TipoCuentaRepositoryAdapter(TipoCuentaJpaRepository jpaRepository) {
        logs.setLogger(TipoCuentaRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TipoCuenta> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE TIPO DE CUENTA POR ID " + id);
        Optional<TipoCuenta> tipoCuenta = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(tipoCuenta.orElse(null)));
        return tipoCuenta;
    }

    @Override
    public List<TipoCuenta> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS TIPOS DE CUENTA");
        List<TipoCuenta> tipos = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(tipos));
        return tipos;
    }

    @Override
    public TipoCuenta save(TipoCuenta tipoCuenta) {
        logs.info(Const.ML_INI + Util.toJson(tipoCuenta));
        TipoCuenta tipoGuardado = jpaRepository.save(tipoCuenta);
        logs.info(Const.ML_FIN + Util.toJson(tipoGuardado));
        return tipoGuardado;
    }
}
