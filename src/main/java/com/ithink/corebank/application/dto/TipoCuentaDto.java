package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link TipoCuenta}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoCuentaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idTipoCuenta;

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
     * Convierte una entidad {@link TipoCuenta} a su DTO.
     *
     * @param tipoCuenta entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static TipoCuentaDto from(TipoCuenta tipoCuenta) {
        if (tipoCuenta == null) {
            return null;
        }
        TipoCuentaDto dto = new TipoCuentaDto();
        dto.setIdTipoCuenta(tipoCuenta.getIdTipoCuenta());
        dto.setCodigo(tipoCuenta.getCodigo());
        dto.setNombre(tipoCuenta.getNombre());
        dto.setDescripcion(tipoCuenta.getDescripcion());
        dto.setMontoMinimo(tipoCuenta.getMontoMinimo());
        dto.setMontoMaximo(tipoCuenta.getMontoMaximo());
        dto.setFCreacion(tipoCuenta.getFCreacion());
        dto.setFModificacion(tipoCuenta.getFModificacion());
        dto.setUsuarioCreacion(tipoCuenta.getUsuarioCreacion());
        dto.setUsuarioModificacion(tipoCuenta.getUsuarioModificacion());
        return dto;
    }
}
