package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.PersonaDetalle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO HTTP de {@link PersonaDetalle}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDetalleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPersonaDetalle;

    private Integer idPersona;

    private String estadoCivil;

    private String nacionalidad;

    private String ocupacion;

    private String nivelEducativo;

    private String empleador;

    private BigDecimal ingresos;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte la entidad a DTO.
     *
     * @param entity entidad origen
     * @return DTO o {@code null}
     */
    public static PersonaDetalleDto from(PersonaDetalle entity) {
        if (entity == null) {
            return null;
        }
        PersonaDetalleDto dto = new PersonaDetalleDto();
        dto.setIdPersonaDetalle(entity.getIdPersonaDetalle());
        dto.setIdPersona(entity.getIdPersona());
        dto.setEstadoCivil(entity.getEstadoCivil());
        dto.setNacionalidad(entity.getNacionalidad());
        dto.setOcupacion(entity.getOcupacion());
        dto.setNivelEducativo(entity.getNivelEducativo());
        dto.setEmpleador(entity.getEmpleador());
        dto.setIngresos(entity.getIngresos());
        dto.setFCreacion(entity.getFCreacion());
        dto.setFModificacion(entity.getFModificacion());
        dto.setUsuarioCreacion(entity.getUsuarioCreacion());
        dto.setUsuarioModificacion(entity.getUsuarioModificacion());
        return dto;
    }
}
