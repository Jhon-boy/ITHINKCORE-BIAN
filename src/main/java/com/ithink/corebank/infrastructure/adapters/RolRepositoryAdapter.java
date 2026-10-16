package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Rol;
import com.ithink.corebank.domain.ports.RolRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.RolJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link RolRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class RolRepositoryAdapter implements RolRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final RolJpaRepository jpaRepository;

    public RolRepositoryAdapter(RolJpaRepository jpaRepository) {
        logs.setLogger(RolRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE ROL POR ID " + id);
        Optional<Rol> rol = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(rol.orElse(null)));
        return rol;
    }

    @Override
    public List<Rol> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS ROLES");
        List<Rol> roles = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(roles));
        return roles;
    }

    @Override
    public Rol save(Rol rol) {
        logs.info(Const.ML_INI + Util.toJson(rol));
        Rol rolGuardado = jpaRepository.save(rol);
        logs.info(Const.ML_FIN + Util.toJson(rolGuardado));
        return rolGuardado;
    }
}
