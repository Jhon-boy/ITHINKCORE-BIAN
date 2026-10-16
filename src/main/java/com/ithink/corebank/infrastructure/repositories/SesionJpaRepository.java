package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la tabla {@code TSESION}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface SesionJpaRepository extends JpaRepository<Sesion, UUID> {

    @Query("SELECT s FROM Sesion s WHERE s.fCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Sesion> findBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio,
                                  @Param("fechaFin") LocalDateTime fechaFin);
}
