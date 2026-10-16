package com.ithink.corebank.application.dto;

import com.ithink.corebank.domain.entities.Saldo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de la entidad {@link Saldo} con sus atributos escalares.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSaldo;

    private Integer idCuenta;

    private BigDecimal montoContable;

    private BigDecimal montoDisponible;

    private BigDecimal montoBloqueado;

    private BigDecimal montoPorEfectivizar;

    private String estado;

    private String comentario;

    private LocalDateTime fCreacion;

    private LocalDateTime fModificacion;

    private String usuarioCreacion;

    private String usuarioModificacion;

    /**
     * Convierte una entidad {@link Saldo} a su DTO.
     *
     * @param saldo entidad origen
     * @return DTO con los datos escalares o {@code null} si la entidad es nula
     */
    public static SaldoDto from(Saldo saldo) {
        if (saldo == null) {
            return null;
        }
        SaldoDto dto = new SaldoDto();
        dto.setIdSaldo(saldo.getIdSaldo());
        dto.setIdCuenta(saldo.getIdCuenta());
        dto.setMontoContable(saldo.getMontoContable());
        dto.setMontoDisponible(saldo.getMontoDisponible());
        dto.setMontoBloqueado(saldo.getMontoBloqueado());
        dto.setMontoPorEfectivizar(saldo.getMontoPorEfectivizar());
        dto.setEstado(saldo.getEstado());
        dto.setComentario(saldo.getComentario());
        dto.setFCreacion(saldo.getFCreacion());
        dto.setFModificacion(saldo.getFModificacion());
        dto.setUsuarioCreacion(saldo.getUsuarioCreacion());
        dto.setUsuarioModificacion(saldo.getUsuarioModificacion());
        return dto;
    }
}
