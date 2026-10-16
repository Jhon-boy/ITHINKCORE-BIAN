package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.RolDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.RolService;
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
 * Mantenimiento de Rol.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/rol")
@Tag(name = "Mantenimiento - Rol", description = "Altas de Rol")
public class RolMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final RolService rolService;

    /**
     * Crea el controlador de mantenimiento de Rol.
     *
     * @param rolService servicio de Rol
     */
    public RolMantenimientoController(RolService rolService) {
        logs.setLogger(RolMantenimientoController.class);
        this.rolService = rolService;
    }

    /**
     * Registra un Rol.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Rol")
    public ResponseModel registrar(@RequestBody RolDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/rol/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(rolService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
