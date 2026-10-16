package com.ithink.corebank.domain.ports;

import com.ithink.corebank.domain.entities.Persona;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de acceso a datos para la entidad {@link Persona}.
 *
 * @author Jhon
 * @version 1.0
 */
public interface PersonaRepositoryPort {

    Optional<Persona> findById(Integer id);

    List<Persona> findAll();

    Persona save(Persona persona);
}
