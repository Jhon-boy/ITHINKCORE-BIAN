package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.SaldoHistorico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link SaldoHistorico} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoHistoricoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSaldoHistorico;

    private Integer idCuenta;

    private BigDecimal montoContable;

    private BigDecimal montoDisponible;

    private BigDecimal montoBloqueado;

    private BigDecimal montoPorEfectivizar;

    private String estado;

    private String comentario;

    private LocalDateTime fCreacion;

    private String usuarioCreacion;

    /**
     * Convierte una entidad {@link SaldoHistorico} a su DTO.
     *
     * @param saldoHistorico entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static SaldoHistoricoDto from(SaldoHistorico saldoHistorico) {
        if (saldoHistorico == null) {
            return null;
        }
        SaldoHistoricoDto dto = new SaldoHistoricoDto();
        dto.setIdSaldoHistorico(saldoHistorico.getIdSaldoHistorico());
        dto.setIdCuenta(saldoHistorico.getIdCuenta());
        dto.setMontoContable(saldoHistorico.getMontoContable());
        dto.setMontoDisponible(saldoHistorico.getMontoDisponible());
        dto.setMontoBloqueado(saldoHistorico.getMontoBloqueado());
        dto.setMontoPorEfectivizar(saldoHistorico.getMontoPorEfectivizar());
        dto.setEstado(saldoHistorico.getEstado());
        dto.setComentario(saldoHistorico.getComentario());
        dto.setFCreacion(saldoHistorico.getFCreacion());
        dto.setUsuarioCreacion(saldoHistorico.getUsuarioCreacion());
        return dto;
    }
}
