package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.SaldoDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SaldoService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mantenimiento de Saldo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/saldo")
@Tag(name = "Mantenimiento - Saldo", description = "Altas de Saldo")
public class SaldoMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final SaldoService saldoService;

    /**
     * Crea el controlador de mantenimiento de Saldo.
     *
     * @param saldoService servicio de Saldo
     */
    public SaldoMantenimientoController(SaldoService saldoService) {
        logs.setLogger(SaldoMantenimientoController.class);
        this.saldoService = saldoService;
    }

    /**
     * Registra un Saldo.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Saldo")
    public ResponseModel registrar(@RequestBody SaldoDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/saldo/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(saldoService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
