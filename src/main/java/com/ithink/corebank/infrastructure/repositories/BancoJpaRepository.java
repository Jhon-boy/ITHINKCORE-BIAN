package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la tabla {@code TBANCO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface BancoJpaRepository extends JpaRepository<Banco, Integer> {
}
