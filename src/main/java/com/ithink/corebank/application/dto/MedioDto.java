package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Medio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Medio}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idMedio;

    private String codigo;

    private String nombre;

    private String descripcion;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Medio} a su DTO.
     *
     * @param medio entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static MedioDto from(Medio medio) {
        if (medio == null) {
            return null;
        }
        MedioDto dto = new MedioDto();
        dto.setIdMedio(medio.getIdMedio());
        dto.setCodigo(medio.getCodigo());
        dto.setNombre(medio.getNombre());
        dto.setDescripcion(medio.getDescripcion());
        dto.setFCreacion(medio.getFCreacion());
        dto.setFModificacion(medio.getFModificacion());
        dto.setUsuarioCreacion(medio.getUsuarioCreacion());
        dto.setUsuarioModificacion(medio.getUsuarioModificacion());
        return dto;
    }
}
