package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.TipoOperacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link TipoOperacion}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoOperacionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTipoOperacion;

    private String codigo;

    private String nombre;

    private String descripcion;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link TipoOperacion} a su DTO.
     *
     * @param tipoOperacion entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static TipoOperacionDto from(TipoOperacion tipoOperacion) {
        if (tipoOperacion == null) {
            return null;
        }
        TipoOperacionDto dto = new TipoOperacionDto();
        dto.setIdTipoOperacion(tipoOperacion.getIdTipoOperacion());
        dto.setCodigo(tipoOperacion.getCodigo());
        dto.setNombre(tipoOperacion.getNombre());
        dto.setDescripcion(tipoOperacion.getDescripcion());
        dto.setFCreacion(tipoOperacion.getFCreacion());
        dto.setFModificacion(tipoOperacion.getFModificacion());
        dto.setUsuarioCreacion(tipoOperacion.getUsuarioCreacion());
        dto.setUsuarioModificacion(tipoOperacion.getUsuarioModificacion());
        return dto;
    }
}
