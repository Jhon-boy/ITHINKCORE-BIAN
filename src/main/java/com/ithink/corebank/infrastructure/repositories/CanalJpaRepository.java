package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Canal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TCANAL}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface CanalJpaRepository extends JpaRepository<Canal, Integer> {
}
