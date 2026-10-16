package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio Spring Data JPA para la tabla {@code TTRANSACCION}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface TransaccionJpaRepository extends JpaRepository<Transaccion, Integer> {

    @Query("SELECT t FROM Transaccion t WHERE t.fCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Transaccion> findBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio,
                                       @Param("fechaFin") LocalDateTime fechaFin);
}
