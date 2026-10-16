package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Cuenta} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCuenta;

    private Integer idTipoCuenta;

    private Integer idPersona;

    private String numeroCuenta;

    private Boolean interna;

    private String codigoBanco;

    private String nombreBanco;

    private String estado;

    private String comentario;

    private String vigencia;

    private String caducidad;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Cuenta} a su DTO.
     *
     * @param cuenta entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static CuentaDto from(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }
        CuentaDto dto = new CuentaDto();
        dto.setIdCuenta(cuenta.getIdCuenta());
        dto.setIdTipoCuenta(cuenta.getIdTipoCuenta());
        dto.setIdPersona(cuenta.getIdPersona());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setInterna(cuenta.getInterna());
        dto.setCodigoBanco(cuenta.getCodigoBanco());
        dto.setNombreBanco(cuenta.getNombreBanco());
        dto.setEstado(cuenta.getEstado());
        dto.setComentario(cuenta.getComentario());
        dto.setVigencia(cuenta.getVigencia());
        dto.setCaducidad(cuenta.getCaducidad());
        dto.setFCreacion(cuenta.getFCreacion());
        dto.setFModificacion(cuenta.getFModificacion());
        dto.setUsuarioCreacion(cuenta.getUsuarioCreacion());
        dto.setUsuarioModificacion(cuenta.getUsuarioModificacion());
        return dto;
    }
}
