package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.EstadoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link EstadoMovimiento}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoMovimientoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEstadoMovimiento;

    private String codigo;

    private String nombre;

    private String descripcion;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link EstadoMovimiento} a su DTO.
     *
     * @param estadoMovimiento entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static EstadoMovimientoDto from(EstadoMovimiento estadoMovimiento) {
        if (estadoMovimiento == null) {
            return null;
        }
        EstadoMovimientoDto dto = new EstadoMovimientoDto();
        dto.setIdEstadoMovimiento(estadoMovimiento.getIdEstadoMovimiento());
        dto.setCodigo(estadoMovimiento.getCodigo());
        dto.setNombre(estadoMovimiento.getNombre());
        dto.setDescripcion(estadoMovimiento.getDescripcion());
        dto.setFCreacion(estadoMovimiento.getFCreacion());
        dto.setFModificacion(estadoMovimiento.getFModificacion());
        dto.setUsuarioCreacion(estadoMovimiento.getUsuarioCreacion());
        dto.setUsuarioModificacion(estadoMovimiento.getUsuarioModificacion());
        return dto;
    }
}
