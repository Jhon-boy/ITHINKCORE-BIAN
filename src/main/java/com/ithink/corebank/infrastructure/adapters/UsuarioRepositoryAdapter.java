package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Usuario;
import com.ithink.corebank.domain.ports.UsuarioRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.UsuarioJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link UsuarioRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        logs.setLogger(UsuarioRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE USUARIO POR ID " + id);
        Optional<Usuario> usuario = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(usuario.orElse(null)));
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS USUARIOS");
        List<Usuario> usuarios = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(usuarios));
        return usuarios;
    }

    @Override
    public Usuario save(Usuario usuario) {
        logs.info(Const.ML_INI + Util.toJson(usuario));
        Usuario usuarioGuardado = jpaRepository.save(usuario);
        logs.info(Const.ML_FIN + Util.toJson(usuarioGuardado));
        return usuarioGuardado;
    }
}
