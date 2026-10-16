package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TMODULO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface ModuloJpaRepository extends JpaRepository<Modulo, Integer> {
}
