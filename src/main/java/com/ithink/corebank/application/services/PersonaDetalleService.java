package com.ithink.corebank.application.services;

import com.ithink.corebank.application.dto.PersonaDetalleDto;
import com.ithink.corebank.domain.entities.Persona;
import com.ithink.corebank.domain.entities.PersonaDetalle;
import com.ithink.corebank.domain.ports.PersonaDetalleRepositoryPort;
import com.ithink.corebank.domain.ports.PersonaRepositoryPort;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Casos de uso de {@code PersonaDetalle} (detalle demográfico/laboral).
 *
 * @author Jhon
 * @version 1.0
 */
@Service
public class PersonaDetalleService {

    private static final Loggers logs = new Loggers();

    private final PersonaDetalleRepositoryPort personaDetalleRepositoryPort;
    private final PersonaRepositoryPort personaRepositoryPort;

    /**
     * Crea el servicio de PersonaDetalle.
     *
     * @param personaDetalleRepositoryPort puerto de detalle
     * @param personaRepositoryPort puerto de persona
     */
    public PersonaDetalleService(
            PersonaDetalleRepositoryPort personaDetalleRepositoryPort,
            PersonaRepositoryPort personaRepositoryPort) {
        logs.setLogger(PersonaDetalleService.class);
        this.personaDetalleRepositoryPort = personaDetalleRepositoryPort;
        this.personaRepositoryPort = personaRepositoryPort;
    }

    /**
     * Consulta un detalle por id.
     *
     * @param id identificador
     * @return DTO encontrado
     */
    @Transactional(readOnly = true)
    public PersonaDetalleDto findById(Integer id) {
        logs.info(Const.ML_INI + "CONSULTA DE PERSONA DETALLE POR ID " + id);
        PersonaDetalle entity = personaDetalleRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PERSONA DETALLE NO ENCONTRADO: " + id));
        PersonaDetalleDto dto = PersonaDetalleDto.from(entity);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Consulta el detalle por id de persona.
     *
     * @param idPersona identificador de persona
     * @return DTO encontrado
     */
    @Transactional(readOnly = true)
    public PersonaDetalleDto findByIdPersona(Integer idPersona) {
        logs.info(Const.ML_INI + "CONSULTA DE PERSONA DETALLE POR IDPERSONA " + idPersona);
        PersonaDetalle entity = personaDetalleRepositoryPort.findByIdPersona(idPersona)
                .orElseThrow(() -> new IllegalArgumentException(
                        "PERSONA DETALLE NO ENCONTRADO PARA PERSONA: " + idPersona));
        PersonaDetalleDto dto = PersonaDetalleDto.from(entity);
        logs.info(Const.ML_FIN + Util.toJson(dto));
        return dto;
    }

    /**
     * Lista todos los detalles.
     *
     * @return lista de DTOs
     */
    @Transactional(readOnly = true)
    public List<PersonaDetalleDto> findAll() {
        logs.info(Const.ML_INI + "CONSULTA DE TODOS LOS PERSONA DETALLE");
        List<PersonaDetalleDto> list = personaDetalleRepositoryPort.findAll().stream()
                .map(PersonaDetalleDto::from)
                .toList();
        logs.info(Const.ML_FIN + Util.toJson(list));
        return list;
    }

    /**
     * Registra o actualiza el detalle de una persona (1:1).
     *
     * @param dto datos del detalle
     * @return DTO persistido
     */
    @Transactional
    public PersonaDetalleDto save(PersonaDetalleDto dto) {
        logs.info(Const.ML_INI + Util.toJson(dto));
        if (dto.getIdPersona() == null) {
            throw new IllegalArgumentException("IDPERSONA ES REQUERIDO");
        }
        Persona persona = personaRepositoryPort.findById(dto.getIdPersona())
                .orElseThrow(() -> new IllegalArgumentException(
                        "PERSONA NO ENCONTRADA: " + dto.getIdPersona()));

        PersonaDetalle entity = personaDetalleRepositoryPort.findByIdPersona(dto.getIdPersona())
                .orElseGet(PersonaDetalle::new);

        LocalDateTime now = LocalDateTime.now();
        if (entity.getIdPersonaDetalle() == null) {
            entity.setFCreacion(now);
            entity.setUsuarioCreacion(dto.getUsuarioCreacion() != null ? dto.getUsuarioCreacion() : "API");
        }
        entity.setPersona(persona);
        entity.setIdPersona(dto.getIdPersona());
        entity.setEstadoCivil(dto.getEstadoCivil());
        entity.setNacionalidad(dto.getNacionalidad());
        entity.setOcupacion(dto.getOcupacion());
        entity.setNivelEducativo(dto.getNivelEducativo());
        entity.setEmpleador(dto.getEmpleador());
        entity.setIngresos(dto.getIngresos());
        entity.setFModificacion(now);
        entity.setUsuarioModificacion(dto.getUsuarioModificacion() != null
                ? dto.getUsuarioModificacion()
                : "API");

        PersonaDetalle saved = personaDetalleRepositoryPort.save(entity);
        PersonaDetalleDto response = PersonaDetalleDto.from(saved);
        logs.info(Const.ML_FIN + Util.toJson(response));
        return response;
    }
}
