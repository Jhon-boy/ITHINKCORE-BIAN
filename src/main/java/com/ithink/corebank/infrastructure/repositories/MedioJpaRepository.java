package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Medio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TMEDIO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface MedioJpaRepository extends JpaRepository<Medio, Integer> {
}
