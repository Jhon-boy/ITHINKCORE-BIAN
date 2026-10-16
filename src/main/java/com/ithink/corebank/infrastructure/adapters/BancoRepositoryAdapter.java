package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Banco;
import com.ithink.corebank.domain.ports.BancoRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.BancoJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link BancoRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class BancoRepositoryAdapter implements BancoRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final BancoJpaRepository jpaRepository;

    public BancoRepositoryAdapter(BancoJpaRepository jpaRepository) {
        logs.setLogger(BancoRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Optional<Banco> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE BANCO POR ID " + id);
        Optional<Banco> banco = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(banco.orElse(null)));
        return banco;
    }

    @Override
    public List<Banco> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS BANCOS");
        List<Banco> bancos = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(bancos));
        return bancos;
    }

    @Override
    public Banco save(Banco banco) {
        logs.info(Const.ML_INI + Util.toJson(banco));
        Banco bancoGuardado = jpaRepository.save(banco);
        logs.info(Const.ML_FIN + Util.toJson(bancoGuardado));
        return bancoGuardado;
    }
}
