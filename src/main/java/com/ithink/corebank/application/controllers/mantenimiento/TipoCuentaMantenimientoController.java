package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.TipoCuentaDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.TipoCuentaService;
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
 * Mantenimiento de TipoCuenta.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/tipo-cuenta")
@Tag(name = "Mantenimiento - TipoCuenta", description = "Altas de TipoCuenta")
public class TipoCuentaMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final TipoCuentaService tipoCuentaService;

    /**
     * Crea el controlador de mantenimiento de TipoCuenta.
     *
     * @param tipoCuentaService servicio de TipoCuenta
     */
    public TipoCuentaMantenimientoController(TipoCuentaService tipoCuentaService) {
        logs.setLogger(TipoCuentaMantenimientoController.class);
        this.tipoCuentaService = tipoCuentaService;
    }

    /**
     * Registra un TipoCuenta.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar TipoCuenta")
    public ResponseModel registrar(@RequestBody TipoCuentaDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/tipo-cuenta/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(tipoCuentaService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
