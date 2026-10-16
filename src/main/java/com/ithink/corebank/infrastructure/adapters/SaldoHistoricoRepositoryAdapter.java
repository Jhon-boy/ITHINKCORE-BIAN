package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.SaldoHistorico;
import com.ithink.corebank.domain.ports.SaldoHistoricoRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.SaldoHistoricoJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link SaldoHistoricoRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class SaldoHistoricoRepositoryAdapter implements SaldoHistoricoRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final SaldoHistoricoJpaRepository jpaRepository;

    public SaldoHistoricoRepositoryAdapter(SaldoHistoricoJpaRepository jpaRepository) {
        logs.setLogger(SaldoHistoricoRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<SaldoHistorico> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDO HISTORICO POR ID " + id);
        Optional<SaldoHistorico> saldoHistorico = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(saldoHistorico.orElse(null)));
        return saldoHistorico;
    }

    @Override
    public List<SaldoHistorico> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDOS HISTORICOS ENTRE " + fechaInicio + " Y " + fechaFin);
        List<SaldoHistorico> saldos = jpaRepository.findBetweenDates(fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + Util.toJson(saldos));
        return saldos;
    }

    @Override
    public SaldoHistorico save(SaldoHistorico saldoHistorico) {
        logs.info(Const.ML_INI + Util.toJson(saldoHistorico));
        SaldoHistorico saldoGuardado = jpaRepository.save(saldoHistorico);
        logs.info(Const.ML_FIN + Util.toJson(saldoGuardado));
        return saldoGuardado;
    }
}
