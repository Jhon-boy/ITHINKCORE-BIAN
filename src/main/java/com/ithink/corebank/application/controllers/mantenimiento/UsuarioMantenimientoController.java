package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.UsuarioDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.UsuarioService;
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
 * Mantenimiento de Usuario.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/usuario")
@Tag(name = "Mantenimiento - Usuario", description = "Altas de Usuario")
public class UsuarioMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final UsuarioService usuarioService;

    /**
     * Crea el controlador de mantenimiento de Usuario.
     *
     * @param usuarioService servicio de Usuario
     */
    public UsuarioMantenimientoController(UsuarioService usuarioService) {
        logs.setLogger(UsuarioMantenimientoController.class);
        this.usuarioService = usuarioService;
    }

    /**
     * Registra un Usuario.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Usuario")
    public ResponseModel registrar(@RequestBody UsuarioDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/usuario/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(usuarioService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
