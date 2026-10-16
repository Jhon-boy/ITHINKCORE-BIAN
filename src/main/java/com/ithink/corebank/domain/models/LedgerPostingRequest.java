package com.ithink.corebank.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Solicitud de dominio para iniciar o postear un movimiento en el ledger.
 * No es un DTO HTTP: se construye desde {@code MovimientoDto} o mappers BIAN.
 *
 * @author Jhon
 * @version 1.0
 */
@Getter
@Builder
public class LedgerPostingRequest {

    /** Identificador de la cuenta origen. */
    private final Integer idCuenta;

    /** Tipo de movimiento: {@code C} crédito o {@code D} débito. */
    private final String tipo;

    /** Monto del movimiento. */
    private final BigDecimal monto;

    /** Identificador de cuenta contraparte (transferencia interna). */
    private final Integer idCuentaContraparte;

    /** Número de cuenta contraparte. */
    private final String numeroCuentaContraparte;

    /** Nombre de la contraparte. */
    private final String nombreContraparte;

    /** Código del banco contraparte. */
    private final String codigoBancoContraparte;

    /** Nombre del banco contraparte. */
    private final String nombreBancoContraparte;

    /** Identificador del método de operación. */
    private final Integer idMetodoOperacion;

    /** Identificador del tipo de transacción. */
    private final Integer idTransaccion;

    /** Identificador del canal. */
    private final Integer idCanal;

    /** Identificador de la sucursal. */
    private final Integer idSucursal;

    /** Identificador del medio. */
    private final Integer idMedio;

    /** Mensaje operativo del movimiento. */
    private final String mensaje;

    /** Comentario adicional. */
    private final String comentario;

    /** Referencia de negocio. */
    private final String referencia;

    /** UUID externo opcional. */
    private final String uuid;

    /** Código de rastreo. */
    private final String codigoRastreo;

    /** Usuario que origina la operación. */
    private final String usuario;
}
