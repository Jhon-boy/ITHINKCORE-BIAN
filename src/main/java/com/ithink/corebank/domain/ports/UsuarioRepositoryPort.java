package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Usuario}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface UsuarioRepositoryPort {

    Optional<Usuario> findById(Integer id);

    List<Usuario> findAll();

    Usuario save(Usuario usuario);
}
