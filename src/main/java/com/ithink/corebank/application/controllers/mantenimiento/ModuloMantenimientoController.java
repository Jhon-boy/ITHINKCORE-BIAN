package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.ModuloDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.ModuloService;
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
 * Mantenimiento de Modulo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/modulo")
@Tag(name = "Mantenimiento - Modulo", description = "Altas de Modulo")
public class ModuloMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final ModuloService moduloService;

    /**
     * Crea el controlador de mantenimiento de Modulo.
     *
     * @param moduloService servicio de Modulo
     */
    public ModuloMantenimientoController(ModuloService moduloService) {
        logs.setLogger(ModuloMantenimientoController.class);
        this.moduloService = moduloService;
    }

    /**
     * Registra un Modulo.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Modulo")
    public ResponseModel registrar(@RequestBody ModuloDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/modulo/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(moduloService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
