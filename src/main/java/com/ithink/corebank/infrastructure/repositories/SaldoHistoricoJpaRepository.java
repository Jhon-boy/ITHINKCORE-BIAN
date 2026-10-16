package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.SaldoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio Spring Data JPA para la tabla {@code TSALDOHISTORICO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface SaldoHistoricoJpaRepository extends JpaRepository<SaldoHistorico, Integer> {

    @Query("SELECT s FROM SaldoHistorico s WHERE s.fCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<SaldoHistorico> findBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio,
                                          @Param("fechaFin") LocalDateTime fechaFin);
}
