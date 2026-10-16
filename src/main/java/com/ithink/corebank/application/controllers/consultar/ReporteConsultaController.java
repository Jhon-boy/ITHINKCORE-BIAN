package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.ReporteService;
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
 * Consultas de Reporte.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/reporte")
@Tag(name = "Consultar - Reporte", description = "Consultas de Reporte")
public class ReporteConsultaController {

    private static final Loggers logs = new Loggers();

    private final ReporteService reporteService;

    /**
     * Crea el controlador de consulta de Reporte.
     *
     * @param reporteService servicio de Reporte
     */
    public ReporteConsultaController(ReporteService reporteService) {
        logs.setLogger(ReporteConsultaController.class);
        this.reporteService = reporteService;
    }

    /**
     * Obtiene un registro de Reporte por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Reporte por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/reporte/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(reporteService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Lista registros de Reporte en un rango de fechas.
     *
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Reporte por rango de fechas")
    public ResponseModel listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "GET /api/consultar/reporte " + fechaInicio + " - " + fechaFin);
        try {
            ResponseModel response = Util.generateOKResponse(reporteService.findAll(fechaInicio, fechaFin));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
