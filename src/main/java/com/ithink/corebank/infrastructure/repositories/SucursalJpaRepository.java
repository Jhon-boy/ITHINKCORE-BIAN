package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TSUCURSAL}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface SucursalJpaRepository extends JpaRepository<Sucursal, Integer> {
}
