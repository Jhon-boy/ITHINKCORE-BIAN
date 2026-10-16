package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA que interactua directamente con la tabla
 * {@code TDISPOSITIVO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface DispositivoJpaRepository extends JpaRepository<Dispositivo, Integer> {
}
