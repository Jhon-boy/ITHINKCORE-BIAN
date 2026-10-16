package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.MetodoOperacionDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.MetodoOperacionService;
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
 * Mantenimiento de MetodoOperacion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/metodo-operacion")
@Tag(name = "Mantenimiento - MetodoOperacion", description = "Altas de MetodoOperacion")
public class MetodoOperacionMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final MetodoOperacionService metodoOperacionService;

    /**
     * Crea el controlador de mantenimiento de MetodoOperacion.
     *
     * @param metodoOperacionService servicio de MetodoOperacion
     */
    public MetodoOperacionMantenimientoController(MetodoOperacionService metodoOperacionService) {
        logs.setLogger(MetodoOperacionMantenimientoController.class);
        this.metodoOperacionService = metodoOperacionService;
    }

    /**
     * Registra un MetodoOperacion.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar MetodoOperacion")
    public ResponseModel registrar(@RequestBody MetodoOperacionDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/metodo-operacion/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(metodoOperacionService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
