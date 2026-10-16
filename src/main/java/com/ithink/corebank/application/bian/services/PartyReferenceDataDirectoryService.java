package com.ithink.corebank.application.bian.services;

import com.ithink.bian.model.PartyControl;
import com.ithink.bian.model.PartyReferenceDataDirectoryEntry;
import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.application.bian.exception.BianNotFoundException;
import com.ithink.corebank.application.bian.mapper.PartyBianMapper;
import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Casos de uso BIAN del Service Domain Party Reference Data Directory (slice CR).
 * Persiste y consulta personas del directorio sin alterar la API consultar/mantenimiento de persona.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class PartyReferenceDataDirectoryService {

    private static final Loggers logs = new Loggers();

    private final PersonaRepositoryPort personaRepositoryPort;
    private final PartyBianMapper partyBianMapper;

    /**
     * Crea el servicio de Party Reference Data Directory.
     *
     * @param personaRepositoryPort puerto de personas
     * @param partyBianMapper mapper BIAN
     */
    public PartyReferenceDataDirectoryService(PersonaRepositoryPort personaRepositoryPort, PartyBianMapper partyBianMapper) {
        logs.setLogger(PartyReferenceDataDirectoryService.class);
        this.personaRepositoryPort = personaRepositoryPort;
        this.partyBianMapper = partyBianMapper;
    }

    /**
     * InCR Register: registra una persona en el directorio BIAN.
     *
     * @param entry payload BIAN
     * @return entrada registrada
     */
    @Transactional
    public PartyReferenceDataDirectoryEntry register(PartyReferenceDataDirectoryEntry entry) {
        logs.info(Const.ML_INI + "BIAN Register PartyReferenceDataDirectory");
        validateEntry(entry);

        Persona persona = new Persona();
        partyBianMapper.applyToPersona(entry, persona);

        if (persona.getIdentificacion() == null || persona.getIdentificacion().isBlank()) {
            throw new BianBadRequestException("PartyIdentification.IdentifierValue is required");
        }
        if (persona.getNombres() == null || persona.getNombres().isBlank()) {
            throw new BianBadRequestException("PartyName.Name is required");
        }
        if (persona.getTipoIdentificacion() == null) {
            persona.setTipoIdentificacion("CEDULA");
        }
        if (persona.getEstado() == null) {
            persona.setEstado("ACTIVO");
        }

        LocalDateTime now = LocalDateTime.now();
        persona.setFCreacion(now);
        persona.setFModificacion(now);
        persona.setUsuarioCreacion("BIAN");
        persona.setUsuarioModificacion("BIAN");

        Persona saved = personaRepositoryPort.save(persona);
        PartyReferenceDataDirectoryEntry response = partyBianMapper.toBianEntry(saved);
        logs.info(Const.ML_FIN + "BIAN Register id=" + saved.getIdPersona());
        return response;
    }

    /**
     * ReCR Retrieve: consulta una entrada del directorio por id (idPersona).
     *
     * @param partyreferencedatadirectoryid identificador de la entrada
     * @return entrada encontrada
     */
    @Transactional(readOnly = true)
    public PartyReferenceDataDirectoryEntry retrieve(String partyreferencedatadirectoryid) {
        logs.info(Const.ML_INI + "BIAN Retrieve id=" + partyreferencedatadirectoryid);
        Persona persona = findPersonaOrThrow(partyreferencedatadirectoryid);
        PartyReferenceDataDirectoryEntry response = partyBianMapper.toBianEntry(persona);
        logs.info(Const.ML_FIN + "BIAN Retrieve id=" + partyreferencedatadirectoryid);
        return response;
    }

    /**
     * UpCR Update: actualiza detalles de una entrada del directorio.
     *
     * @param partyreferencedatadirectoryid identificador de la entrada
     * @param entry payload BIAN
     * @return entrada actualizada
     */
    @Transactional
    public PartyReferenceDataDirectoryEntry update(String partyreferencedatadirectoryid, PartyReferenceDataDirectoryEntry entry) {
        logs.info(Const.ML_INI + "BIAN Update id=" + partyreferencedatadirectoryid);
        validateEntry(entry);
        Persona persona = findPersonaOrThrow(partyreferencedatadirectoryid);
        partyBianMapper.applyToPersona(entry, persona);
        persona.setFModificacion(LocalDateTime.now());
        persona.setUsuarioModificacion("BIAN");
        Persona saved = personaRepositoryPort.save(persona);
        PartyReferenceDataDirectoryEntry response = partyBianMapper.toBianEntry(saved);
        logs.info(Const.ML_FIN + "BIAN Update id=" + partyreferencedatadirectoryid);
        return response;
    }

    /**
     * Activa o desactiva al cliente.
     *
     * @param partyreferencedatadirectoryid identificador de la persona
     * @param control payload con PartyStatus
     * @return vigencia resultante
     */
    @Transactional
    public PartyControl control(String partyreferencedatadirectoryid, PartyControl control) {
        logs.info(Const.ML_INI + "BIAN Control id=" + partyreferencedatadirectoryid);
        if (control == null || control.getPartyStatus() == null || control.getPartyStatus().isBlank()) {
            throw new BianBadRequestException("PartyStatus is required");
        }
        String status = control.getPartyStatus().trim().toUpperCase();
        if (!"ACTIVO".equals(status) && !"INACTIVO".equals(status)) {
            throw new BianBadRequestException("PartyStatus must be ACTIVO or INACTIVO");
        }
        Persona persona = findPersonaOrThrow(partyreferencedatadirectoryid);
        persona.setEstado(status);
        persona.setFModificacion(LocalDateTime.now());
        persona.setUsuarioModificacion("BIAN");
        Persona saved = personaRepositoryPort.save(persona);
        PartyControl response = new PartyControl();
        response.setPartyReference(String.valueOf(saved.getIdPersona()));
        response.setPartyStatus(saved.getEstado());
        response.setMessage("Party status set to " + saved.getEstado());
        logs.info(Const.ML_FIN + "BIAN Control estado=" + saved.getEstado());
        return response;
    }

    private Persona findPersonaOrThrow(String partyreferencedatadirectoryid) {
        Integer id = parseId(partyreferencedatadirectoryid);
        return personaRepositoryPort.findById(id)
                .orElseThrow(() -> new BianNotFoundException(
                        "PartyReferenceDataDirectoryEntry not found: " + partyreferencedatadirectoryid));
    }

    private Integer parseId(String partyreferencedatadirectoryid) {
        if (partyreferencedatadirectoryid == null || partyreferencedatadirectoryid.isBlank()) {
            throw new BianBadRequestException("partyreferencedatadirectoryid is required");
        }
        try {
            return Integer.valueOf(partyreferencedatadirectoryid.trim());
        } catch (NumberFormatException ex) {
            throw new BianBadRequestException(
                    "partyreferencedatadirectoryid must be a numeric id: " + partyreferencedatadirectoryid);
        }
    }

    private void validateEntry(PartyReferenceDataDirectoryEntry entry) {
        if (entry == null || entry.getPartyReference() == null) {
            throw new BianBadRequestException("PartyReference is required");
        }
    }
}
