package com.ithink.corebank.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "TBANCO", schema = "dbo")
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBANCO", nullable = false)
    private Integer idBanco;

    @Column(name = "CODIGOBANCO", unique = true)
    private String codigoBanco;

    @Column(name = "NOMBREBANCO")
    private String nombreBanco;

    @Column(name = "CODIGOSWIFT")
    private String codigoSwift;

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
