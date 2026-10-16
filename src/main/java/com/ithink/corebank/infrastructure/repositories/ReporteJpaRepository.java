package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio Spring Data JPA para la tabla {@code TREPORTE}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface ReporteJpaRepository extends JpaRepository<Reporte, Integer> {

    @Query("SELECT r FROM Reporte r WHERE r.fechaContable BETWEEN :fechaInicio AND :fechaFin")
    List<Reporte> findBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio,
                                   @Param("fechaFin") LocalDateTime fechaFin);
}
