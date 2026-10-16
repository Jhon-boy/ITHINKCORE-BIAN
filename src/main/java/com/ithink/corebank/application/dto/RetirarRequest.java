package com.ithink.corebank.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Solicitud HTTP para retirar fondos de una cuenta.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetirarRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCuenta;

    private BigDecimal monto;

    private String mensaje;

    private String comentario;

    private String referencia;

    private String usuario;
}
