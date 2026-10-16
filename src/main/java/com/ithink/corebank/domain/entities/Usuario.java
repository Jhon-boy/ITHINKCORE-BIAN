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
@Table(name = "TUSUARIO", schema = "dbo")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO", nullable = false)
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDSUCURSAL")
    private Sucursal sucursal;

    @Column(name = "IDSUCURSAL", insertable = false, updatable = false)
    private Integer idSucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPERSONA")
    private Persona persona;

    @Column(name = "IDPERSONA", insertable = false, updatable = false)
    private Integer idPersona;

    @Column(name = "USUARIO", unique = true)
    private String usuario;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "TEMPORAL")
    private Boolean temporal;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
