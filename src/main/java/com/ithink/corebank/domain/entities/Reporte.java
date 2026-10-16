package com.ithink.corebank.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "TREPORTE", schema = "dbo")
public class Reporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IDREPORTE", nullable = false)
    private Integer idReporte;

    @Column(name = "FECHAMOVIMIENTO")
    private LocalDateTime fechaMovimiento;

    @Column(name = "FECHACONTABLE")
    private LocalDateTime fechaContable;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "TRANSACCION")
    private String transaccion;

    @Column(name = "MONTOMOVIMIENTO")
    private String montoMovimiento;

    @Column(name = "NOMBREORDENANTE")
    private String nombreOrdenante;

    @Column(name = "BANCOORDENANTE")
    private String bancoOrdenante;

    @Column(name = "IDENTIFICACIONORDENANTE")
    private String identificacionOrdenante;

    @Column(name = "NOMBREBENEFICIARIO")
    private String nombreBeneficiario;

    @Column(name = "BANCOBENEFICIARIO")
    private String bancoBeneficiario;

    @Column(name = "IDENTIFICACIONBENEFICIARIO")
    private String identificacionBeneficiario;

    @Column(name = "SALDOANTERIOR")
    private BigDecimal saldoAnterior;

    @Column(name = "SALDOACTUAL")
    private BigDecimal saldoActual;

    @Column(name = "CANAL")
    private String canal;

    @Column(name = "REFERENCIA")
    private String referencia;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "USUARIOEJECUCION")
    private String usuarioEjecucion;

    @Column(name = "CUENTAORDENANTE")
    private String cuentaOrdenante;

    @Column(name = "CUENTABENEFICIARIO")
    private String cuentaBeneficiario;

    @Column(name = "IDCUENTA")
    private Integer idCuenta;
}
