package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.MetodoOperacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link MetodoOperacion} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoOperacionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idMetodoOperacion;

    private Integer idTipoOperacion;

    private String codigo;

    private String nombre;

    private String descripcion;

    private BigDecimal montoMinimo;

    private BigDecimal montoMaximo;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link MetodoOperacion} a su DTO.
     *
     * @param metodoOperacion entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static MetodoOperacionDto from(MetodoOperacion metodoOperacion) {
        if (metodoOperacion == null) {
            return null;
        }
        MetodoOperacionDto dto = new MetodoOperacionDto();
        dto.setIdMetodoOperacion(metodoOperacion.getIdMetodoOperacion());
        dto.setIdTipoOperacion(metodoOperacion.getIdTipoOperacion());
        dto.setCodigo(metodoOperacion.getCodigo());
        dto.setNombre(metodoOperacion.getNombre());
        dto.setDescripcion(metodoOperacion.getDescripcion());
        dto.setMontoMinimo(metodoOperacion.getMontoMinimo());
        dto.setMontoMaximo(metodoOperacion.getMontoMaximo());
        dto.setFCreacion(metodoOperacion.getFCreacion());
        dto.setFModificacion(metodoOperacion.getFModificacion());
        dto.setUsuarioCreacion(metodoOperacion.getUsuarioCreacion());
        dto.setUsuarioModificacion(metodoOperacion.getUsuarioModificacion());
        return dto;
    }
}
