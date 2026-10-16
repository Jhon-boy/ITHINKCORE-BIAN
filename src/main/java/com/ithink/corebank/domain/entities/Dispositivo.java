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
@Table(name = "TDISPOSITIVO", schema = "dbo")
public class Dispositivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDISPOSITIVO", nullable = false)
    private Integer idDispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO")
    private Usuario usuario;

    @Column(name = "IDUSUARIO", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "IMEI", unique = true)
    private String imei;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "MODELO")
    private String modelo;

    @Column(name = "ULTIMOACCESO")
    private LocalDateTime ultimoAcceso;

    @Column(name = "SISTEMAOPERATIVO")
    private String sistemaOperativo;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
