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
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TSESION", schema = "dbo")
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "IDSESION", nullable = false)
    private UUID idSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO")
    private Usuario usuario;

    @Column(name = "IDUSUARIO", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "TOKEN")
    private UUID token;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FEXPIRACION")
    private LocalDateTime fExpiracion;

    @Column(name = "ACTIVO")
    private Boolean activo;
}
