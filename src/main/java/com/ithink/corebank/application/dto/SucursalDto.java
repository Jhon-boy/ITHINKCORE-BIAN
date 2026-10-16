package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Sucursal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Sucursal}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSucursal;

    private String nombre;

    private String direccion;

    private Boolean estado;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Sucursal} a su DTO.
     *
     * @param sucursal entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static SucursalDto from(Sucursal sucursal) {
        if (sucursal == null) {
            return null;
        }
        SucursalDto dto = new SucursalDto();
        dto.setIdSucursal(sucursal.getIdSucursal());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());
        dto.setEstado(sucursal.getEstado());
        dto.setFCreacion(sucursal.getFCreacion());
        dto.setFModificacion(sucursal.getFModificacion());
        dto.setUsuarioCreacion(sucursal.getUsuarioCreacion());
        dto.setUsuarioModificacion(sucursal.getUsuarioModificacion());
        return dto;
    }
}
