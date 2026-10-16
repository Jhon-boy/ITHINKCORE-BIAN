package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Canal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Canal}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanalDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCanal;

    private String codigo;

    private String nombre;

    private String descripcion;

    private String estado;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Canal} a su DTO.
     *
     * @param canal entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static CanalDto from(Canal canal) {
        if (canal == null) {
            return null;
        }
        CanalDto dto = new CanalDto();
        dto.setIdCanal(canal.getIdCanal());
        dto.setCodigo(canal.getCodigo());
        dto.setNombre(canal.getNombre());
        dto.setDescripcion(canal.getDescripcion());
        dto.setEstado(canal.getEstado());
        dto.setFCreacion(canal.getFCreacion());
        dto.setFModificacion(canal.getFModificacion());
        dto.setUsuarioCreacion(canal.getUsuarioCreacion());
        dto.setUsuarioModificacion(canal.getUsuarioModificacion());
        return dto;
    }
}
