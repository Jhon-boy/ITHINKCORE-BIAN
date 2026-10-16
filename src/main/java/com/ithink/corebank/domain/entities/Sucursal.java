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
@Table(name = "TSUCURSAL", schema = "dbo")
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSUCURSAL", nullable = false)
    private Integer idSucursal;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "ESTADO")
    private Boolean estado;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
