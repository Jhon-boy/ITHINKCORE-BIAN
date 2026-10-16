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
@Table(name = "TTRANSACCION", schema = "dbo")
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDTRANSACCION", nullable = false)
    private Integer idTransaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMODULO")
    private Modulo modulo;

    @Column(name = "IDMODULO", insertable = false, updatable = false)
    private Integer idModulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCANAL")
    private Canal canal;

    @Column(name = "IDCANAL", insertable = false, updatable = false)
    private Integer idCanal;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
