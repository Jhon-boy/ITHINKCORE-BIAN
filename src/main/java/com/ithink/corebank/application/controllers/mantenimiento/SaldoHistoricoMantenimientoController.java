package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.SaldoHistoricoDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SaldoHistoricoService;
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
 * Mantenimiento de SaldoHistorico.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/saldo-historico")
@Tag(name = "Mantenimiento - SaldoHistorico", description = "Altas de SaldoHistorico")
public class SaldoHistoricoMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final SaldoHistoricoService saldoHistoricoService;

    /**
     * Crea el controlador de mantenimiento de SaldoHistorico.
     *
     * @param saldoHistoricoService servicio de SaldoHistorico
     */
    public SaldoHistoricoMantenimientoController(SaldoHistoricoService saldoHistoricoService) {
        logs.setLogger(SaldoHistoricoMantenimientoController.class);
        this.saldoHistoricoService = saldoHistoricoService;
    }

    /**
     * Registra un SaldoHistorico.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar SaldoHistorico")
    public ResponseModel registrar(@RequestBody SaldoHistoricoDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/saldo-historico/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(saldoHistoricoService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
