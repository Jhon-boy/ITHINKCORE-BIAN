package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Persona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Persona}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPersona;

    private String identificacion;

    private String nombres;

    private String apellidos;

    private LocalDateTime fNacimiento;

    private String genero;

    private String correo;

    private String telefono;

    private String direccion;

    private String tipoIdentificacion;

    private String estado;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Persona} a su DTO.
     *
     * @param persona entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static PersonaDto from(Persona persona) {
        if (persona == null) {
            return null;
        }
        PersonaDto dto = new PersonaDto();
        dto.setIdPersona(persona.getIdPersona());
        dto.setIdentificacion(persona.getIdentificacion());
        dto.setNombres(persona.getNombres());
        dto.setApellidos(persona.getApellidos());
        dto.setFNacimiento(persona.getFNacimiento());
        dto.setGenero(persona.getGenero());
        dto.setCorreo(persona.getCorreo());
        dto.setTelefono(persona.getTelefono());
        dto.setDireccion(persona.getDireccion());
        dto.setTipoIdentificacion(persona.getTipoIdentificacion());
        dto.setEstado(persona.getEstado());
        dto.setFCreacion(persona.getFCreacion());
        dto.setFModificacion(persona.getFModificacion());
        dto.setUsuarioCreacion(persona.getUsuarioCreacion());
        dto.setUsuarioModificacion(persona.getUsuarioModificacion());
        return dto;
    }
}
