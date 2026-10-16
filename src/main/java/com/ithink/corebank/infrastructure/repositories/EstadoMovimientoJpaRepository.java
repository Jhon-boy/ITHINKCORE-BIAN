package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.EstadoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la tabla {@code TESTADOMOVIMIENTO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface EstadoMovimientoJpaRepository extends JpaRepository<EstadoMovimiento, Integer> {

    Optional<EstadoMovimiento> findByCodigo(String codigo);
}
