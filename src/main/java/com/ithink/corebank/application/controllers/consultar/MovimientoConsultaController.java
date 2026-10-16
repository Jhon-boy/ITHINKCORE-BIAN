package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.MovimientoService;
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
 * Consultas de movimientos.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/movimiento")
@Tag(name = "Consultar - Movimiento", description = "Consultas de movimientos / histórico operativo")
public class MovimientoConsultaController {

    private static final Loggers logs = new Loggers();

    private final MovimientoService movimientoService;

    /**
     * Crea el controlador de consulta de movimiento.
     *
     * @param movimientoService servicio de movimiento
     */
    public MovimientoConsultaController(MovimientoService movimientoService) {
        logs.setLogger(MovimientoConsultaController.class);
        this.movimientoService = movimientoService;
    }

    /**
     * Obtiene un movimiento por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/movimiento/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(movimientoService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Lista movimientos en un rango de fechas.
     *
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar movimientos por rango de fechas")
    public ResponseModel listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "GET /api/consultar/movimiento " + fechaInicio + " - " + fechaFin);
        try {
            ResponseModel response =
                    Util.generateOKResponse(movimientoService.findAll(fechaInicio, fechaFin));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
