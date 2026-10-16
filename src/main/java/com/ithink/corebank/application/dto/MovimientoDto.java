package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Movimiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Movimiento} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idMovimiento;

    private Integer idCuenta;

    private Integer idSaldoHistorico;

    private Integer idMetodoOperacion;

    private Integer idTransaccion;

    private Integer idCanal;

    private Integer idSucursal;

    private Integer idMedio;

    private Integer idEstadoMovimiento;

    private Integer idCuentaContraparte;

    private String tipo;

    private BigDecimal debito;

    private BigDecimal monto;

    private LocalDateTime fechaContable;

    private LocalDateTime fechaReal;

    private String numeroCuentaContraparte;

    private String nombreContraparte;

    private String codigoBancoContraparte;

    private String nombreBancoContraparte;

    private String codigoRastreo;

    private String motivoRechazo;

    private String mensaje;

    private String comentario;

    private String uuid;

    private String referencia;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Movimiento} a su DTO.
     *
     * @param movimiento entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static MovimientoDto from(Movimiento movimiento) {
        if (movimiento == null) {
            return null;
        }
        MovimientoDto dto = new MovimientoDto();
        dto.setIdMovimiento(movimiento.getIdMovimiento());
        dto.setIdCuenta(movimiento.getIdCuenta());
        dto.setIdSaldoHistorico(movimiento.getIdSaldoHistorico());
        dto.setIdMetodoOperacion(movimiento.getIdMetodoOperacion());
        dto.setIdTransaccion(movimiento.getIdTransaccion());
        dto.setIdCanal(movimiento.getIdCanal());
        dto.setIdSucursal(movimiento.getIdSucursal());
        dto.setIdMedio(movimiento.getIdMedio());
        dto.setIdEstadoMovimiento(movimiento.getIdEstadoMovimiento());
        dto.setIdCuentaContraparte(movimiento.getIdCuentaContraparte());
        dto.setTipo(movimiento.getTipo());
        dto.setDebito(movimiento.getDebito());
        dto.setMonto(movimiento.getMonto());
        dto.setFechaContable(movimiento.getFechaContable());
        dto.setFechaReal(movimiento.getFechaReal());
        dto.setNumeroCuentaContraparte(movimiento.getNumeroCuentaContraparte());
        dto.setNombreContraparte(movimiento.getNombreContraparte());
        dto.setCodigoBancoContraparte(movimiento.getCodigoBancoContraparte());
        dto.setNombreBancoContraparte(movimiento.getNombreBancoContraparte());
        dto.setCodigoRastreo(movimiento.getCodigoRastreo());
        dto.setMotivoRechazo(movimiento.getMotivoRechazo());
        dto.setMensaje(movimiento.getMensaje());
        dto.setComentario(movimiento.getComentario());
        dto.setUuid(movimiento.getUuid());
        dto.setReferencia(movimiento.getReferencia());
        dto.setFCreacion(movimiento.getFCreacion());
        dto.setFModificacion(movimiento.getFModificacion());
        dto.setUsuarioCreacion(movimiento.getUsuarioCreacion());
        dto.setUsuarioModificacion(movimiento.getUsuarioModificacion());
        return dto;
    }
}
