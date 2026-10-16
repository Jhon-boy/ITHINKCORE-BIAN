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
@Table(name = "TPERSONA", schema = "dbo")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPERSONA", nullable = false)
    private Integer idPersona;

    @Column(name = "IDENTIFICACION", unique = true)
    private String identificacion;

    @Column(name = "NOMBRES")
    private String nombres;

    @Column(name = "APELLIDOS")
    private String apellidos;

    @Column(name = "FNACIMIENTO")
    private LocalDateTime fNacimiento;

    @Column(name = "GENERO")
    private String genero;

    @Column(name = "CORREO")
    private String correo;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "TIPOIDENTIFICACION")
    private String tipoIdentificacion;

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
