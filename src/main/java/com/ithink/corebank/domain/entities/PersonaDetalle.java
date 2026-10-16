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

/**
 * Detalle demográfico/laboral de una {@link Persona} (tabla {@code TPERSONADETALLE}).
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TPERSONADETALLE", schema = "dbo")
public class PersonaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPERSONADETALLE", nullable = false)
    private Integer idPersonaDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPERSONA", nullable = false)
    private Persona persona;

    @Column(name = "IDPERSONA", insertable = false, updatable = false)
    private Integer idPersona;

    @Column(name = "ESTADOCIVIL")
    private String estadoCivil;

    @Column(name = "NACIONALIDAD")
    private String nacionalidad;

    @Column(name = "OCUPACION")
    private String ocupacion;

    @Column(name = "NIVELEDUCATIVO")
    private String nivelEducativo;

    @Column(name = "EMPLEADOR")
    private String empleador;

    @Column(name = "INGRESOS")
    private BigDecimal ingresos;

    @Column(name = "FCREACION")
    private LocalDateTime fCreacion;

    @Column(name = "FMODIFICACION")
    private LocalDateTime fModificacion;

    @Column(name = "USUARIOCREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIOMODIFICACION")
    private String usuarioModificacion;
}
