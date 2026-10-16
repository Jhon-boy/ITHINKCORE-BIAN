package com.ithink.corebank.application.bian.handlers;

import com.ithink.bian.model.HTTPError;
import com.ithink.corebank.application.bian.exception.BianBadRequestException;
import com.ithink.corebank.application.bian.exception.BianNotFoundException;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maneja excepciones de los controladores BIAN y responde con {@link HTTPError}.
 *
 * @author Jhon
 * @version 1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.ithink.corebank.application.bian")
public class BianExceptionHandler {

    private static final Loggers LOGS = new Loggers();

    /**
     * Constructor que inicializa el logger del manejador.
     */
    public BianExceptionHandler() {
        LOGS.setLogger(BianExceptionHandler.class);
    }

    /**
     * Maneja recursos BIAN no encontrados.
     *
     * @param ex excepción de negocio
     * @return respuesta HTTP 404
     */
    @ExceptionHandler(BianNotFoundException.class)
    public ResponseEntity<HTTPError> handleNotFound(BianNotFoundException ex) {
        LOGS.error(Const.ML_ERROR + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError("404", "NotFound", ex.getMessage()));
    }

    /**
     * Maneja solicitudes BIAN inválidas.
     *
     * @param ex excepción de negocio
     * @return respuesta HTTP 400
     */
    @ExceptionHandler(BianBadRequestException.class)
    public ResponseEntity<HTTPError> handleBadRequest(BianBadRequestException ex) {
        LOGS.error(Const.ML_ERROR + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError("400", "BadRequest", ex.getMessage()));
    }

    /**
     * Maneja errores no controlados en el paquete BIAN.
     *
     * @param ex excepción inesperada
     * @return respuesta HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HTTPError> handleUnexpected(Exception ex) {
        LOGS.error(Const.ML_ERROR + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError("500", "InternalServerError", ex.getMessage()));
    }

    private HTTPError buildError(String code, String status, String message) {
        HTTPError httpError = new HTTPError();
        httpError.setStatusCode(code);
        httpError.setStatus(status);
        httpError.setMessage(message);
        return httpError;
    }
}
