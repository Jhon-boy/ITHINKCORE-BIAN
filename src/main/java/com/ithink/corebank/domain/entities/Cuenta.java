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
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TCUENTA", schema = "dbo")
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCUENTA", nullable = false)
    private Integer idCuenta;

    @Column(name = "NUMEROCUENTA")
    private String numeroCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDTIPOCUENTA")
    private TipoCuenta tipoCuenta;

    @Column(name = "IDTIPOCUENTA", insertable = false, updatable = false)
    private Integer idTipoCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPERSONA")
    private Persona persona;

    @Column(name = "IDPERSONA", insertable = false, updatable = false)
    private Integer idPersona;

    @Column(name = "INTERNA")
    private Boolean interna;

    @Column(name = "CODIGOBANCO")
    private String codigoBanco;

    @Column(name = "NOMBREBANCO")
    private String nombreBanco;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "VIGENCIA")
    private String vigencia;

    @Column(name = "CADUCIDAD")
    private String caducidad;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
