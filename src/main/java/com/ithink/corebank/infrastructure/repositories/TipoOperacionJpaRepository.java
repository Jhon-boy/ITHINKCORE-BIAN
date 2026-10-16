package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TTIPOOPERACION}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface TipoOperacionJpaRepository extends JpaRepository<TipoOperacion, Integer> {
}
