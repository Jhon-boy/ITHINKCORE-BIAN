package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Reporte;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Reporte}.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idReporte;

    private LocalDateTime fechaMovimiento;

    private LocalDateTime fechaContable;

    private String tipo;

    private String transaccion;

    private String montoMovimiento;

    private String nombreOrdenante;

    private String bancoOrdenante;

    private String identificacionOrdenante;

    private String nombreBeneficiario;

    private String bancoBeneficiario;

    private String identificacionBeneficiario;

    private BigDecimal saldoAnterior;

    private BigDecimal saldoActual;

    private String canal;

    private String referencia;

    private String comentario;

    private String usuarioEjecucion;

    private String cuentaOrdenante;

    private String cuentaBeneficiario;

    private Integer idCuenta;

    /**
     * Convierte una entidad {@link Reporte} a su DTO.
     *
     * @param reporte entidad origen
     * @return DTO o {@code null} si la entidad es nula
     */
    public static ReporteDto from(Reporte reporte) {
        if (reporte == null) {
            return null;
        }
        ReporteDto dto = new ReporteDto();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setFechaMovimiento(reporte.getFechaMovimiento());
        dto.setFechaContable(reporte.getFechaContable());
        dto.setTipo(reporte.getTipo());
        dto.setTransaccion(reporte.getTransaccion());
        dto.setMontoMovimiento(reporte.getMontoMovimiento());
        dto.setNombreOrdenante(reporte.getNombreOrdenante());
        dto.setBancoOrdenante(reporte.getBancoOrdenante());
        dto.setIdentificacionOrdenante(reporte.getIdentificacionOrdenante());
        dto.setNombreBeneficiario(reporte.getNombreBeneficiario());
        dto.setBancoBeneficiario(reporte.getBancoBeneficiario());
        dto.setIdentificacionBeneficiario(reporte.getIdentificacionBeneficiario());
        dto.setSaldoAnterior(reporte.getSaldoAnterior());
        dto.setSaldoActual(reporte.getSaldoActual());
        dto.setCanal(reporte.getCanal());
        dto.setReferencia(reporte.getReferencia());
        dto.setComentario(reporte.getComentario());
        dto.setUsuarioEjecucion(reporte.getUsuarioEjecucion());
        dto.setCuentaOrdenante(reporte.getCuentaOrdenante());
        dto.setCuentaBeneficiario(reporte.getCuentaBeneficiario());
        dto.setIdCuenta(reporte.getIdCuenta());
        return dto;
    }
}
