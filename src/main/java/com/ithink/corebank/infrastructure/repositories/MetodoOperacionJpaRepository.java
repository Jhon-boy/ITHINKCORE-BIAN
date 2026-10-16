package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.MetodoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TMETODOOPERACION}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface MetodoOperacionJpaRepository extends JpaRepository<MetodoOperacion, Integer> {
}
