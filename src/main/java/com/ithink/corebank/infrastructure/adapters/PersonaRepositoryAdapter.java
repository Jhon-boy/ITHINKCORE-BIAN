package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.PersonaJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link PersonaRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class PersonaRepositoryAdapter implements PersonaRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final PersonaJpaRepository jpaRepository;

    public PersonaRepositoryAdapter(PersonaJpaRepository jpaRepository) {
        logs.setLogger(PersonaRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Persona> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE PERSONA POR ID " + id);
        Optional<Persona> persona = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + Util.toJson(persona.orElse(null)));
        return persona;
    }

    @Override
    public List<Persona> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODAS LAS PERSONAS");
        List<Persona> personas = jpaRepository.findAll();
        logs.info(Const.ML_FIN + Util.toJson(personas));
        return personas;
    }

    @Override
    public Persona save(Persona persona) {
        logs.info(Const.ML_INI + Util.toJson(persona));
        Persona personaGuardada = jpaRepository.save(persona);
        logs.info(Const.ML_FIN + Util.toJson(personaGuardada));
        return personaGuardada;
    }
}
