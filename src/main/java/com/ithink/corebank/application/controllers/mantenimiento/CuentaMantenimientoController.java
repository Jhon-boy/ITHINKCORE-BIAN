package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.CuentaDto;
import com.ithink.corebank.application.dto.DepositarRequest;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.dto.RetirarRequest;
import com.ithink.corebank.application.services.CuentaService;
import com.ithink.corebank.application.services.OperacionCuentaService;
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
 * Mantenimiento de cuenta: alta y operaciones sobre saldo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/cuenta")
@Tag(name = "Mantenimiento - Cuenta",
        description = "Registrar cuenta; depositar y retirar")
public class CuentaMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final CuentaService cuentaService;
    private final OperacionCuentaService operacionCuentaService;

    /**
     * Crea el controlador de mantenimiento de cuenta.
     *
     * @param cuentaService servicio de cuenta
     * @param operacionCuentaService servicio de operaciones
     */
    public CuentaMantenimientoController(
            CuentaService cuentaService,
            OperacionCuentaService operacionCuentaService) {
        logs.setLogger(CuentaMantenimientoController.class);
        this.cuentaService = cuentaService;
        this.operacionCuentaService = operacionCuentaService;
    }

    /**
     * Registra una cuenta.
     *
     * @param dto datos de la cuenta
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar cuenta", description = "Alta en TCUENTA")
    public ResponseModel registrar(@RequestBody CuentaDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/cuenta/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(cuentaService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Deposita fondos en una cuenta.
     *
     * @param request datos del depósito
     * @return respuesta HTTP con el movimiento
     */
    @PostMapping("/depositar")
    @Operation(summary = "Depositar", description = "Crédito inmediato (BOOKED) vía ledger")
    public ResponseModel depositar(@RequestBody DepositarRequest request) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/cuenta/depositar");
        try {
            ResponseModel response =
                    Util.generateOKResponse(operacionCuentaService.depositar(request));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Retira fondos de una cuenta.
     *
     * @param request datos del retiro
     * @return respuesta HTTP con el movimiento
     */
    @PostMapping("/retirar")
    @Operation(summary = "Retirar", description = "Débito inmediato (BOOKED) vía ledger")
    public ResponseModel retirar(@RequestBody RetirarRequest request) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/cuenta/retirar");
        try {
            ResponseModel response =
                    Util.generateOKResponse(operacionCuentaService.retirar(request));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
