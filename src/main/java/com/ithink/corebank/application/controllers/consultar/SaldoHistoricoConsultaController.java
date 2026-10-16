package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SaldoHistoricoService;
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
 * Consultas de SaldoHistorico.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/saldo-historico")
@Tag(name = "Consultar - SaldoHistorico", description = "Consultas de SaldoHistorico")
public class SaldoHistoricoConsultaController {

    private static final Loggers logs = new Loggers();

    private final SaldoHistoricoService saldoHistoricoService;

    /**
     * Crea el controlador de consulta de SaldoHistorico.
     *
     * @param saldoHistoricoService servicio de SaldoHistorico
     */
    public SaldoHistoricoConsultaController(SaldoHistoricoService saldoHistoricoService) {
        logs.setLogger(SaldoHistoricoConsultaController.class);
        this.saldoHistoricoService = saldoHistoricoService;
    }

    /**
     * Obtiene un registro de SaldoHistorico por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener SaldoHistorico por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/saldo-historico/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(saldoHistoricoService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Lista registros de SaldoHistorico en un rango de fechas.
     *
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar SaldoHistorico por rango de fechas")
    public ResponseModel listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "GET /api/consultar/saldo-historico " + fechaInicio + " - " + fechaFin);
        try {
            ResponseModel response = Util.generateOKResponse(saldoHistoricoService.findAll(fechaInicio, fechaFin));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
