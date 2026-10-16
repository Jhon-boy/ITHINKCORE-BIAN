package com.ithink.corebank.application.bian.exception;

/**
 * Solicitud BIAN inválida (HTTP 400).
 *
 * @author Jhon
 * @version 1.0
 */
public class BianBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea una excepción de solicitud inválida.
     *
     * @param message detalle del error
     */
    public BianBadRequestException(String message) {
        super(message);
    }
}
