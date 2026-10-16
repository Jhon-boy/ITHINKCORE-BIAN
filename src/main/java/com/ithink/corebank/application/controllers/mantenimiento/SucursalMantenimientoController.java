package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.SucursalDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SucursalService;
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
 * Mantenimiento de Sucursal.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/sucursal")
@Tag(name = "Mantenimiento - Sucursal", description = "Altas de Sucursal")
public class SucursalMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final SucursalService sucursalService;

    /**
     * Crea el controlador de mantenimiento de Sucursal.
     *
     * @param sucursalService servicio de Sucursal
     */
    public SucursalMantenimientoController(SucursalService sucursalService) {
        logs.setLogger(SucursalMantenimientoController.class);
        this.sucursalService = sucursalService;
    }

    /**
     * Registra un Sucursal.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Sucursal")
    public ResponseModel registrar(@RequestBody SucursalDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/sucursal/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(sucursalService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
