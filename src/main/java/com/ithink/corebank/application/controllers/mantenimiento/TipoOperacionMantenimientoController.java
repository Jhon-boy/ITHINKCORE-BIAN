package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.TipoOperacionDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.TipoOperacionService;
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
 * Mantenimiento de TipoOperacion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/tipo-operacion")
@Tag(name = "Mantenimiento - TipoOperacion", description = "Altas de TipoOperacion")
public class TipoOperacionMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final TipoOperacionService tipoOperacionService;

    /**
     * Crea el controlador de mantenimiento de TipoOperacion.
     *
     * @param tipoOperacionService servicio de TipoOperacion
     */
    public TipoOperacionMantenimientoController(TipoOperacionService tipoOperacionService) {
        logs.setLogger(TipoOperacionMantenimientoController.class);
        this.tipoOperacionService = tipoOperacionService;
    }

    /**
     * Registra un TipoOperacion.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar TipoOperacion")
    public ResponseModel registrar(@RequestBody TipoOperacionDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/tipo-operacion/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(tipoOperacionService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
