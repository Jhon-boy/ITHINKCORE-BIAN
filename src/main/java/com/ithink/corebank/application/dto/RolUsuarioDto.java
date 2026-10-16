package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link RolUsuario} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolUsuarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idRolUsuario;

    private Integer idUsuario;

    private Integer idRol;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    private String estado;

    /**
     * Convierte una entidad {@link RolUsuario} a su DTO.
     *
     * @param rolUsuario entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static RolUsuarioDto from(RolUsuario rolUsuario) {
        if (rolUsuario == null) {
            return null;
        }
        RolUsuarioDto dto = new RolUsuarioDto();
        dto.setIdRolUsuario(rolUsuario.getIdRolUsuario());
        dto.setIdUsuario(rolUsuario.getIdUsuario());
        dto.setIdRol(rolUsuario.getIdRol());
        dto.setFCreacion(rolUsuario.getFCreacion());
        dto.setFModificacion(rolUsuario.getFModificacion());
        dto.setUsuarioCreacion(rolUsuario.getUsuarioCreacion());
        dto.setUsuarioModificacion(rolUsuario.getUsuarioModificacion());
        dto.setEstado(rolUsuario.getEstado());
        return dto;
    }
}
