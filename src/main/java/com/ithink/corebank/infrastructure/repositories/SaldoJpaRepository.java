package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la tabla {@code TSALDO}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface SaldoJpaRepository extends JpaRepository<Saldo, Integer> {

    Optional<Saldo> findFirstByIdCuentaOrderByIdSaldoDesc(Integer idCuenta);

    @Query("SELECT s FROM Saldo s WHERE s.fCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Saldo> findBetweenDates(@Param("fechaInicio") LocalDateTime fechaInicio,
                                 @Param("fechaFin") LocalDateTime fechaFin);
}
