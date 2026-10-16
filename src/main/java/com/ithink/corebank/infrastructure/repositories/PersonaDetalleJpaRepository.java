package com.ithink.corebank.infrastructure.repositories;

import com.ithink.corebank.domain.entities.PersonaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA de {@code TPERSONADETALLE}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public interface PersonaDetalleJpaRepository extends JpaRepository<PersonaDetalle, Integer> {

    /**
     * Busca el detalle por el id de persona (relación 1:1).
     *
     * @param idPersona identificador de persona
     * @return detalle opcional
     */
    Optional<PersonaDetalle> findByIdPersona(Integer idPersona);
}
