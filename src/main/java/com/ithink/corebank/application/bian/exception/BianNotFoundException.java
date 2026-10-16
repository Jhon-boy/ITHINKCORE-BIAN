package com.ithink.corebank.application.bian.exception;

/**
 * Recurso BIAN no encontrado (HTTP 404).
 *
 * @author Jhon
 * @version 1.0
 */
public class BianNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea una excepción de recurso no encontrado.
     *
     * @param message detalle del error
     */
    public BianNotFoundException(String message) {
        super(message);
    }
}
