package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TROLUSUARIO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface RolUsuarioJpaRepository extends JpaRepository<RolUsuario, Integer> {
}
