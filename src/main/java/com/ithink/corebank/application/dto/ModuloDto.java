package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Modulo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Modulo}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuloDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idModulo;

    private String codigo;

    private String nombre;

    private String descripcion;

    private String estado;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Modulo} a su DTO.
     *
     * @param modulo entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static ModuloDto from(Modulo modulo) {
        if (modulo == null) {
            return null;
        }
        ModuloDto dto = new ModuloDto();
        dto.setIdModulo(modulo.getIdModulo());
        dto.setCodigo(modulo.getCodigo());
        dto.setNombre(modulo.getNombre());
        dto.setDescripcion(modulo.getDescripcion());
        dto.setEstado(modulo.getEstado());
        dto.setFCreacion(modulo.getFCreacion());
        dto.setFModificacion(modulo.getFModificacion());
        dto.setUsuarioCreacion(modulo.getUsuarioCreacion());
        dto.setUsuarioModificacion(modulo.getUsuarioModificacion());
        return dto;
    }
}
