package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio Spring Data JPA para la tabla {@code TMOVIMIENTO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface MovimientoJpaRepository extends JpaRepository<Movimiento, Integer> {

    @Query("SELECT m FROM Movimiento m WHERE m.fCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Movimiento> findBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio,
                                      @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Movimientos de una cuenta en un rango de creación.
     *
     * @param idCuenta identificador de cuenta
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return movimientos del rango
     */
    @Query("""
            SELECT m FROM Movimiento m
            WHERE m.idCuenta = :idCuenta
              AND m.fCreacion BETWEEN :fechaInicio AND :fechaFin
            """)
    List<Movimiento> findByCuentaBetween(@Param("idCuenta") Integer idCuenta,
                                         @Param("fechaInicio") LocalDateTime fechaInicio,
                                         @Param("fechaFin") LocalDateTime fechaFin);
}
