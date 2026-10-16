package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Sucursal;
import com.ithink.corebank.domain.ports.SucursalRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.SucursalJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link SucursalRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class SucursalRepositoryAdapter implements SucursalRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final SucursalJpaRepository jpaRepository;

    public SucursalRepositoryAdapter(SucursalJpaRepository jpaRepository) {
        logs.setLogger(SucursalRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Sucursal> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE SUCURSAL POR ID " + id);
        Optional<Sucursal> sucursal = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(sucursal.orElse(null)));
        return sucursal;
    }

    @Override
    public List<Sucursal> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODAS LAS SUCURSALES");
        List<Sucursal> sucursales = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(sucursales));
        return sucursales;
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        logs.info(Const.ML_INI + Util.toJson(sucursal));
        Sucursal sucursalGuardada = jpaRepository.save(sucursal);
        logs.info(Const.ML_FIN + Util.toJson(sucursalGuardada));
        return sucursalGuardada;
    }
}
