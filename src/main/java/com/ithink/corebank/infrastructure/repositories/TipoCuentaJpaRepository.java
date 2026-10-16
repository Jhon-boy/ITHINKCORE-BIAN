package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TTIPOCUENTA}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface TipoCuentaJpaRepository extends JpaRepository<TipoCuenta, Integer> {
}
