package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.RolUsuarioDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.RolUsuarioService;
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
 * Mantenimiento de RolUsuario.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/rol-usuario")
@Tag(name = "Mantenimiento - RolUsuario", description = "Altas de RolUsuario")
public class RolUsuarioMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final RolUsuarioService rolUsuarioService;

    /**
     * Crea el controlador de mantenimiento de RolUsuario.
     *
     * @param rolUsuarioService servicio de RolUsuario
     */
    public RolUsuarioMantenimientoController(RolUsuarioService rolUsuarioService) {
        logs.setLogger(RolUsuarioMantenimientoController.class);
        this.rolUsuarioService = rolUsuarioService;
    }

    /**
     * Registra un RolUsuario.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar RolUsuario")
    public ResponseModel registrar(@RequestBody RolUsuarioDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/rol-usuario/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(rolUsuarioService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
