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
@Table(name = "TMOVIMIENTO", schema = "dbo")
public class Movimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDMOVIMIENTO", nullable = false)
    private Integer idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCUENTA")
    private Cuenta cuenta;

    @Column(name = "IDCUENTA", insertable = false, updatable = false)
    private Integer idCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDSALDOHISTORICO")
    private SaldoHistorico saldoHistorico;

    @Column(name = "IDSALDOHISTORICO", insertable = false, updatable = false)
    private Integer idSaldoHistorico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMETODOOPERACION")
    private MetodoOperacion metodoOperacion;

    @Column(name = "IDMETODOOPERACION", insertable = false, updatable = false)
    private Integer idMetodoOperacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDTRANSACCION")
    private Transaccion transaccion;

    @Column(name = "IDTRANSACCION", insertable = false, updatable = false)
    private Integer idTransaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCANAL")
    private Canal canal;

    @Column(name = "IDCANAL", insertable = false, updatable = false)
    private Integer idCanal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDSUCURSAL")
    private Sucursal sucursal;

    @Column(name = "IDSUCURSAL", insertable = false, updatable = false)
    private Integer idSucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMEDIO")
    private Medio medio;

    @Column(name = "IDMEDIO", insertable = false, updatable = false)
    private Integer idMedio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDESTADOMOVIMIENTO")
    private EstadoMovimiento estadoMovimiento;

    @Column(name = "IDESTADOMOVIMIENTO", insertable = false, updatable = false)
    private Integer idEstadoMovimiento;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "DEBITO")
    private BigDecimal debito;

    @Column(name = "MONTO")
    private BigDecimal monto;

    @Column(name = "FECHACONTABLE")
    private LocalDateTime fechaContable;

    @Column(name = "FECHAREAL")
    private LocalDateTime fechaReal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCUENTACONTRAPARTE")
    private Cuenta cuentaContraparte;

    @Column(name = "IDCUENTACONTRAPARTE", insertable = false, updatable = false)
    private Integer idCuentaContraparte;

    @Column(name = "NUMEROCUENTACONTRAPARTE")
    private String numeroCuentaContraparte;

    @Column(name = "NOMBRECONTRAPARTE")
    private String nombreContraparte;

    @Column(name = "CODIGOBANCOCONTRAPARTE")
    private String codigoBancoContraparte;

    @Column(name = "NOMBREBANCOCONTRAPARTE")
    private String nombreBancoContraparte;

    @Column(name = "CODIGORASTREO")
    private String codigoRastreo;

    @Column(name = "MOTIVORECHAZO")
    private String motivoRechazo;

    @Column(name = "MENSAJE")
    private String mensaje;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "REFERENCIA")
    private String referencia;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
