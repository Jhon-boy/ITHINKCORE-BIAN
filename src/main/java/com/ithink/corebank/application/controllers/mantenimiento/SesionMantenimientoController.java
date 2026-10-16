package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.SesionDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SesionService;
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
 * Mantenimiento de Sesion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/sesion")
@Tag(name = "Mantenimiento - Sesion", description = "Altas de Sesion")
public class SesionMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final SesionService sesionService;

    /**
     * Crea el controlador de mantenimiento de Sesion.
     *
     * @param sesionService servicio de Sesion
     */
    public SesionMantenimientoController(SesionService sesionService) {
        logs.setLogger(SesionMantenimientoController.class);
        this.sesionService = sesionService;
    }

    /**
     * Registra un Sesion.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Sesion")
    public ResponseModel registrar(@RequestBody SesionDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/sesion/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(sesionService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
