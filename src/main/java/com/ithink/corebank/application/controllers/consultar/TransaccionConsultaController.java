package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.TransaccionService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Consultas de Transaccion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/transaccion")
@Tag(name = "Consultar - Transaccion", description = "Consultas de Transaccion")
public class TransaccionConsultaController {

    private static final Loggers logs = new Loggers();

    private final TransaccionService transaccionService;

    /**
     * Crea el controlador de consulta de Transaccion.
     *
     * @param transaccionService servicio de Transaccion
     */
    public TransaccionConsultaController(TransaccionService transaccionService) {
        logs.setLogger(TransaccionConsultaController.class);
        this.transaccionService = transaccionService;
    }

    /**
     * Obtiene un registro de Transaccion por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Transaccion por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/transaccion/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(transaccionService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Lista registros de Transaccion en un rango de fechas.
     *
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Transaccion por rango de fechas")
    public ResponseModel listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "GET /api/consultar/transaccion " + fechaInicio + " - " + fechaFin);
        try {
            ResponseModel response = Util.generateOKResponse(transaccionService.findAll(fechaInicio, fechaFin));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
