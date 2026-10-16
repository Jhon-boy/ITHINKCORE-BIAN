package com.ithink.corebank.application.bian.services;

import com.ithink.bian.model.PartyDemographics;
import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.application.bian.exception.BianNotFoundException;
import com.ithink.corebank.application.bian.mapper.DemographicsBianMapper;
import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.entities.PersonaDetalle;
import com.ithink.corebank.domain.ports.PersonaDetalleRepositoryPort;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Casos de uso BIAN del Behavior Qualifier PartyDemographics ({@code TPERSONADETALLE}).
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class DemographicsBianService {

    private static final Loggers logs = new Loggers();

    private final PersonaRepositoryPort personaRepositoryPort;
    private final PersonaDetalleRepositoryPort personaDetalleRepositoryPort;
    private final DemographicsBianMapper demographicsBianMapper;

    /**
     * Crea el servicio BIAN de demografía.
     *
     * @param personaRepositoryPort puerto de personas
     * @param personaDetalleRepositoryPort puerto de detalle
     * @param demographicsBianMapper mapper BIAN
     */
    public DemographicsBianService(
            PersonaRepositoryPort personaRepositoryPort,
            PersonaDetalleRepositoryPort personaDetalleRepositoryPort,
            DemographicsBianMapper demographicsBianMapper) {
        logs.setLogger(DemographicsBianService.class);
        this.personaRepositoryPort = personaRepositoryPort;
        this.personaDetalleRepositoryPort = personaDetalleRepositoryPort;
        this.demographicsBianMapper = demographicsBianMapper;
    }

    /**
     * Registra la demografía de una persona. Si ya existe, la actualiza.
     *
     * @param partyId identificador de persona
     * @param demographics payload BIAN
     * @return demografía persistida
     */
    @Transactional
    public PartyDemographics register(String partyId, PartyDemographics demographics) {
        logs.info(Const.ML_INI + "BIAN PartyDemographics Register id=" + partyId);
        return save(partyId, demographics, true);
    }

    /**
     * Actualiza la demografía ya registrada.
     *
     * @param partyId identificador de persona
     * @param demographics payload BIAN
     * @return demografía actualizada
     */
    @Transactional
    public PartyDemographics update(String partyId, PartyDemographics demographics) {
        logs.info(Const.ML_INI + "BIAN PartyDemographics Update id=" + partyId);
        return save(partyId, demographics, false);
    }

    /**
     * Consulta la demografía de una persona.
     *
     * @param partyId identificador de persona
     * @return demografía BIAN
     */
    @Transactional(readOnly = true)
    public PartyDemographics retrieve(String partyId) {
        logs.info(Const.ML_INI + "BIAN PartyDemographics Retrieve id=" + partyId);
        Integer idPersona = parseId(partyId);
        requirePersona(idPersona, partyId);
        PersonaDetalle detalle = personaDetalleRepositoryPort.findByIdPersona(idPersona)
                .orElseThrow(() -> new BianNotFoundException("PartyDemographics not found for party: " + partyId));
        PartyDemographics response = demographicsBianMapper.toPartyDemographics(detalle);
        logs.info(Const.ML_FIN + "BIAN PartyDemographics Retrieve id=" + partyId);
        return response;
    }

    private PartyDemographics save(String partyId, PartyDemographics demographics, boolean creating) {
        Integer idPersona = parseId(partyId);
        Persona persona = requirePersona(idPersona, partyId);
        PersonaDetalle detalle = personaDetalleRepositoryPort.findByIdPersona(idPersona).orElse(null);
        if (!creating && detalle == null) {
            throw new BianNotFoundException("PartyDemographics not found for party: " + partyId);
        }
        if (detalle == null) {
            detalle = new PersonaDetalle();
            detalle.setFCreacion(LocalDateTime.now());
            detalle.setUsuarioCreacion("BIAN");
        }
        detalle.setPersona(persona);
        detalle.setIdPersona(idPersona);
        demographicsBianMapper.apply(demographics, detalle);
        detalle.setFModificacion(LocalDateTime.now());
        detalle.setUsuarioModificacion("BIAN");
        PersonaDetalle saved = personaDetalleRepositoryPort.save(detalle);
        PartyDemographics response = demographicsBianMapper.toPartyDemographics(saved);
        logs.info(Const.ML_FIN + "BIAN PartyDemographics idDetalle=" + saved.getIdPersonaDetalle());
        return response;
    }

    private Persona requirePersona(Integer idPersona, String partyId) {
        return personaRepositoryPort.findById(idPersona)
                .orElseThrow(() -> new BianNotFoundException(
                        "PartyReferenceDataDirectoryEntry not found: " + partyId));
    }

    private Integer parseId(String partyId) {
        if (partyId == null || partyId.isBlank()) {
            throw new BianBadRequestException("partyreferencedatadirectoryid is required");
        }
        try {
            return Integer.valueOf(partyId.trim());
        } catch (NumberFormatException ex) {
            throw new BianBadRequestException("partyreferencedatadirectoryid must be a numeric id: " + partyId);
        }
    }
}
