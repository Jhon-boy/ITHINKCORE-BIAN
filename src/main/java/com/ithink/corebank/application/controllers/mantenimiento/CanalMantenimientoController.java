package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.CanalDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.CanalService;
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
 * Mantenimiento de Canal.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/canal")
@Tag(name = "Mantenimiento - Canal", description = "Altas de Canal")
public class CanalMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final CanalService canalService;

    /**
     * Crea el controlador de mantenimiento de Canal.
     *
     * @param canalService servicio de Canal
     */
    public CanalMantenimientoController(CanalService canalService) {
        logs.setLogger(CanalMantenimientoController.class);
        this.canalService = canalService;
    }

    /**
     * Registra un Canal.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Canal")
    public ResponseModel registrar(@RequestBody CanalDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/canal/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(canalService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
