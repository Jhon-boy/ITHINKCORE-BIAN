package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TROL}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface RolJpaRepository extends JpaRepository<Rol, Integer> {
}
