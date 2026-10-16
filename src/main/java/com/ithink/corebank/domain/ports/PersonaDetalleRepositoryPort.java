package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.PersonaDetalle;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para {@link PersonaDetalle}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface PersonaDetalleRepositoryPort {

    /**
     * Consulta un detalle por su identificador.
     *
     * @param id identificador del detalle
     * @return detalle encontrado o vacío
     */
    Optional<PersonaDetalle> findById(Integer id);

    /**
     * Consulta el detalle asociado a una persona.
     *
     * @param idPersona identificador de persona
     * @return detalle encontrado o vacío
     */
    Optional<PersonaDetalle> findByIdPersona(Integer idPersona);

    /**
     * Consulta todos los detalles registrados.
     *
     * @return lista de detalles
     */
    List<PersonaDetalle> findAll();

    /**
     * Persiste un detalle de persona.
     *
     * @param personaDetalle entidad a persistir
     * @return entidad persistida
     */
    PersonaDetalle save(PersonaDetalle personaDetalle);
}
