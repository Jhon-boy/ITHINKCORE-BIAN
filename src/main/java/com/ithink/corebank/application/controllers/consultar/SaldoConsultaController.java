package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SaldoService;
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
 * Consultas de saldo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/saldo")
@Tag(name = "Consultar - Saldo", description = "Consultas de saldos por id, cuenta o rango de fechas")
public class SaldoConsultaController {

    private static final Loggers logs = new Loggers();

    private final SaldoService saldoService;

    /**
     * Crea el controlador de consulta de saldo.
     *
     * @param saldoService servicio de saldo
     */
    public SaldoConsultaController(SaldoService saldoService) {
        logs.setLogger(SaldoConsultaController.class);
        this.saldoService = saldoService;
    }

    /**
     * Obtiene un saldo por idSaldo.
     *
     * @param id identificador del saldo
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener saldo por idSaldo")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/saldo/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(saldoService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene el saldo de una cuenta.
     *
     * @param idCuenta identificador de cuenta
     * @return respuesta HTTP
     */
    @GetMapping("/cuenta/{idCuenta}")
    @Operation(summary = "Obtener saldo por idCuenta")
    public ResponseModel obtenerPorCuenta(@PathVariable Integer idCuenta) {
        logs.info(Const.ML_INI + "GET /api/consultar/saldo/cuenta/" + idCuenta);
        try {
            ResponseModel response = Util.generateOKResponse(saldoService.findByIdCuenta(idCuenta));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Lista saldos en un rango de fechas.
     *
     * @param fechaInicio inicio inclusive
     * @param fechaFin fin inclusive
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar saldos por rango de fechas")
    public ResponseModel listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        logs.info(Const.ML_INI + "GET /api/consultar/saldo " + fechaInicio + " - " + fechaFin);
        try {
            ResponseModel response =
                    Util.generateOKResponse(saldoService.findAll(fechaInicio, fechaFin));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
