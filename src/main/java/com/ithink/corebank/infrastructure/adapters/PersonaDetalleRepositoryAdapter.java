package com.ithink.corebank.infrastructure.adapters;

import com.ithink.corebank.domain.entities.PersonaDetalle;
import com.ithink.corebank.domain.ports.PersonaDetalleRepositoryPort;
import com.ithink.corebank.infrastructure.repositories.PersonaDetalleJpaRepository;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa {@link PersonaDetalleRepositoryPort}.
 *
 * @author Jhon
 * @version 1.0
 */
@Repository
public class PersonaDetalleRepositoryAdapter implements PersonaDetalleRepositoryPort {

    private static final Loggers logs = new Loggers();

    private final PersonaDetalleJpaRepository jpaRepository;

    /**
     * Crea el adaptador de PersonaDetalle.
     *
     * @param jpaRepository repositorio JPA
     */
    public PersonaDetalleRepositoryAdapter(PersonaDetalleJpaRepository jpaRepository) {
        logs.setLogger(PersonaDetalleRepositoryAdapter.class);
        this.jpaRepository = jpaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PersonaDetalle> findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE PERSONA DETALLE POR ID " + id);
        Optional<PersonaDetalle> detalle = jpaRepository.findById(id);
        logs.info(Const.ML_FIN + "CONSULTA DE PERSONA DETALLE POR ID " + id);
        return detalle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PersonaDetalle> findByIdPersona(Integer idPersona) {
        logs.info(Const.ML_INI + "CONSULTA DE PERSONA DETALLE POR IDPERSONA " + idPersona);
        Optional<PersonaDetalle> detalle = jpaRepository.findByIdPersona(idPersona);
        logs.info(Const.ML_FIN + "CONSULTA DE PERSONA DETALLE POR IDPERSONA " + idPersona);
        return detalle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PersonaDetalle> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS PERSONA DETALLE");
        List<PersonaDetalle> detalles = jpaRepository.findAll();
        logs.info(Const.ML_FIN + "CONSULTA DE TODOS LOS PERSONA DETALLE count=" + detalles.size());
        return detalles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonaDetalle save(PersonaDetalle personaDetalle) {
        logs.info(Const.ML_INI + "SAVE PERSONA DETALLE idPersona=" + personaDetalle.getIdPersona());
        PersonaDetalle saved = jpaRepository.save(personaDetalle);
        logs.info(Const.ML_FIN + "SAVE PERSONA DETALLE id=" + saved.getIdPersonaDetalle());
        return saved;
    }
}
