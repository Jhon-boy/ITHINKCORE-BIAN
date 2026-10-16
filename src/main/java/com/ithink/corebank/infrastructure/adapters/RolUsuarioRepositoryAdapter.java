package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.RolUsuario;
import com.ithink.corebank.domain.ports.RolUsuarioRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.RolUsuarioJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link RolUsuarioRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class RolUsuarioRepositoryAdapter implements RolUsuarioRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final RolUsuarioJpaRepository jpaRepository;

    public RolUsuarioRepositoryAdapter(RolUsuarioJpaRepository jpaRepository) {
        logs.setLogger(RolUsuarioRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<RolUsuario> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE ROL DE USUARIO POR ID " + id);
        Optional<RolUsuario> rolUsuario = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(rolUsuario.orElse(null)));
        return rolUsuario;
    }

    @Override
    public List<RolUsuario> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS ROLES DE USUARIO");
        List<RolUsuario> roles = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(roles));
        return roles;
    }

    @Override
    public RolUsuario save(RolUsuario rolUsuario) {
        logs.info(Const.ML_INI + Util.toJson(rolUsuario));
        RolUsuario rolGuardado = jpaRepository.save(rolUsuario);
        logs.info(Const.ML_FIN + Util.toJson(rolGuardado));
        return rolGuardado;
    }
}
