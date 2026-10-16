package com.ithink.corebank.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TSALDO", schema = "dbo")
public class Saldo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSALDO", nullable = false)
    private Integer idSaldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCUENTA")
    private Cuenta cuenta;

    @Column(name = "IDCUENTA", insertable = false, updatable = false)
    private Integer idCuenta;

    @Column(name = "MONTOCONTABLE")
    private BigDecimal montoContable;

    @Column(name = "MONTODISPONIBLE")
    private BigDecimal montoDisponible;

    @Column(name = "MONTOBLOQUEADO")
    private BigDecimal montoBloqueado;

    @Column(name = "MONTOPOREFECTIVIZAR")
    private BigDecimal montoPorEfectivizar;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
