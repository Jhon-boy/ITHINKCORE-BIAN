package com.ithink.corebank.application.services;

import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.application.dto.PersonaDto;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que orquesta los casos de uso de {@code Persona}.
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class PersonaService {

    private static final Loggers logs = new Loggers();

    private final PersonaRepositoryPort personaRepositoryPort;

    public PersonaService(PersonaRepositoryPort personaRepositoryPort) {
        logs.setLogger(PersonaService.class);
        this.personaRepositoryPort = personaRepositoryPort;
    }

    /**
     * Consulta una persona por su identificador.
     *
     * @param id identificador de la persona
     * @return DTO de la persona encontrada
     */
    @Transactional(readOnly = true)
    public PersonaDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE PERSONA POR ID " + id);
        Persona persona = personaRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PERSONA NO ENCONTRADA: " + id));
        PersonaDto dto = PersonaDto.from(persona);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta todas las personas registradas.
     *
     * @return lista de DTOs de personas
     */
    @Transactional(readOnly = true)
    public List<PersonaDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODAS LAS PERSONAS");
        List<PersonaDto> personas = personaRepositoryPort.findAll().stream()
                .map(PersonaDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(personas));
        return personas;
    }

    /**
     * Registra una nueva persona.
     *
     * @param dto datos de la persona a registrar
     * @return DTO de la persona persistida
     */
    @Transactional
    public PersonaDto save(PersonaDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        Persona persona = mapToEntity(dto);
        Persona personaGuardada = personaRepositoryPort.save(persona);
        PersonaDto dtoGuardado = PersonaDto.from(personaGuardada);
        logs.info(Const.ML_FIN + Util.toJson(dtoGuardado));
        return dtoGuardado;
    }

    /**
     * Convierte un DTO en una entidad.
     *
     * @param dto DTO de origen
     * @return entidad lista para persistir
     */
    private Persona mapToEntity(PersonaDto dto) {
        Persona persona = new Persona();
        persona.setIdentificacion(dto.getIdentificacion());
        persona.setNombres(dto.getNombres());
        persona.setApellidos(dto.getApellidos());
        persona.setFNacimiento(dto.getFNacimiento());
        persona.setGenero(dto.getGenero());
        persona.setCorreo(dto.getCorreo());
        persona.setTelefono(dto.getTelefono());
        persona.setDireccion(dto.getDireccion());
        persona.setTipoIdentificacion(dto.getTipoIdentificacion());
        persona.setEstado(dto.getEstado());
        return persona;
    }
}
