package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Cuenta;
import com.ithink.corebank.domain.ports.CuentaRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.CuentaJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link CuentaRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class CuentaRepositoryAdapter implements CuentaRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final CuentaJpaRepository jpaRepository;

    public CuentaRepositoryAdapter(CuentaJpaRepository jpaRepository) {
        logs.setLogger(CuentaRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Cuenta> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE CUENTA POR ID " + id);
        Optional<Cuenta> cuenta = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(cuenta.orElse(null)));
        return cuenta;
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        logs.info(Const.ML_INI + "CONSULTA DE CUENTA POR NUMERO " + numeroCuenta);
        Optional<Cuenta> cuenta = jpaRepository.findByNumeroCuenta(numeroCuenta);
        logs.info(Const.ML_FIN + Util.toJson(cuenta.orElse(null)));
        return cuenta;
    }

    @Override
    public List<Cuenta> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODAS LAS CUENTAS");
        List<Cuenta> cuentas = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(cuentas));
        return cuentas;
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        logs.info(Const.ML_INI + Util.toJson(cuenta));
        Cuenta cuentaGuardada = jpaRepository.save(cuenta);
        logs.info(Const.ML_FIN + Util.toJson(cuentaGuardada));
        return cuentaGuardada;
    }
}
