package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Rol}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idRol;

    private String nombre;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String observacion;

    private String estado;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Rol} a su DTO.
     *
     * @param rol entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static RolDto from(Rol rol) {
        if (rol == null) {
            return null;
        }
        RolDto dto = new RolDto();
        dto.setIdRol(rol.getIdRol());
        dto.setNombre(rol.getNombre());
        dto.setFCreacion(rol.getFCreacion());
        dto.setFModificacion(rol.getFModificacion());
        dto.setObservacion(rol.getObservacion());
        dto.setEstado(rol.getEstado());
        dto.setUsuarioCreacion(rol.getUsuarioCreacion());
        dto.setUsuarioModificacion(rol.getUsuarioModificacion());
        return dto;
    }
}
