package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Saldo;
import com.ithink.corebank.domain.ports.SaldoRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.SaldoJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link SaldoRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class SaldoRepositoryAdapter implements SaldoRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final SaldoJpaRepository jpaRepository;

    public SaldoRepositoryAdapter(SaldoJpaRepository jpaRepository) {
        logs.setLogger(SaldoRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Saldo> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDO POR ID " + id);
        Optional<Saldo> saldo = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(saldo.orElse(null)));
        return saldo;
    }

    @Override
    public Optional<Saldo> findByIdCuenta(Integer idCuenta) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDO POR IDCUENTA " + idCuenta);
        Optional<Saldo> saldo = jpaRepository.findFirstByIdCuentaOrderByIdSaldoDesc(idCuenta);
        logs.info(Const.ML_FIN + Util.toJson(saldo.orElse(null)));
        return saldo;
    }

    @Override
    public List<Saldo> findAll(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "CONSULTA DE SALDOS ENTRE " + fechaInicio + " Y " + fechaFin);
        List<Saldo> saldos = jpaRepository.findBetweenDates(fechaInicio, fechaFin);
        logs.info(Const.ML_FIN + Util.toJson(saldos));
        return saldos;
    }

    @Override
    public Saldo save(Saldo saldo) {
        logs.info(Const.ML_INI + Util.toJson(saldo));
        Saldo saldoGuardado = jpaRepository.save(saldo);
        logs.info(Const.ML_FIN + Util.toJson(saldoGuardado));
        return saldoGuardado;
    }
}
