package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.MedioDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.MedioService;
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
 * Mantenimiento de Medio.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/medio")
@Tag(name = "Mantenimiento - Medio", description = "Altas de Medio")
public class MedioMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final MedioService medioService;

    /**
     * Crea el controlador de mantenimiento de Medio.
     *
     * @param medioService servicio de Medio
     */
    public MedioMantenimientoController(MedioService medioService) {
        logs.setLogger(MedioMantenimientoController.class);
        this.medioService = medioService;
    }

    /**
     * Registra un Medio.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Medio")
    public ResponseModel registrar(@RequestBody MedioDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/medio/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(medioService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
