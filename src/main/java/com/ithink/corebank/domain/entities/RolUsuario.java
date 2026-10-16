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
@Table(name = "TROLUSUARIO", schema = "dbo")
public class RolUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDROLUSUARIO", nullable = false)
    private Integer idRolUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO")
    private Usuario usuario;

    @Column(name = "IDUSUARIO", insertable = false, updatable = false)
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDROL")
    private Rol rol;

    @Column(name = "IDROL", insertable = false, updatable = false)
    private Integer idRol;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;

    @Column(name = "ESTADO")
    private String estado;
}
