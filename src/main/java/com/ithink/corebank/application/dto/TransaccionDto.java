package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Transaccion} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTransaccion;

    private Integer idModulo;

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
     * Convierte una entidad {@link Transaccion} a su DTO.
     *
     * @param transaccion entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static TransaccionDto from(Transaccion transaccion) {
        if (transaccion == null) {
            return null;
        }
        TransaccionDto dto = new TransaccionDto();
        dto.setIdTransaccion(transaccion.getIdTransaccion());
        dto.setIdModulo(transaccion.getIdModulo());
        dto.setIdCanal(transaccion.getIdCanal());
        dto.setCodigo(transaccion.getCodigo());
        dto.setNombre(transaccion.getNombre());
        dto.setDescripcion(transaccion.getDescripcion());
        dto.setEstado(transaccion.getEstado());
        dto.setFCreacion(transaccion.getFCreacion());
        dto.setFModificacion(transaccion.getFModificacion());
        dto.setUsuarioCreacion(transaccion.getUsuarioCreacion());
        dto.setUsuarioModificacion(transaccion.getUsuarioModificacion());
        return dto;
    }
}
