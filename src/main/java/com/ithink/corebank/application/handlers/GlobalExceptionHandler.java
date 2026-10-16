package com.ithink.corebank.application.handlers;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador de excepciones global de la aplicacion
 *
 * @author John c
 * @version 1.0
 * */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle Excepciones generales en Ejecucion
     * */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseModel> handleRuntimeException(RuntimeException ex) {
        ResponseModel response = Util.generateBadResponseDefault();
        response.setMessage(Const.MENSAJE_ERROR);
        response.setData(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle genérico de cualquier excepción no contemplada previamente
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleGlobalException(Exception ex){
        ResponseModel response = Util.generateBadResponseDefault();
        response.setMessage(Const.MENSAJE_ERROR);
        response.setData(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
