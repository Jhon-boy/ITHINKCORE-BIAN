package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TPERSONA}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface PersonaJpaRepository extends JpaRepository<Persona, Integer> {
}
