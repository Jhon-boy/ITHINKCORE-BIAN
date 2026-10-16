package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.RolUsuarioService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Consultas de RolUsuario.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/rol-usuario")
@Tag(name = "Consultar - RolUsuario", description = "Consultas de RolUsuario")
public class RolUsuarioConsultaController {

    private static final Loggers logs = new Loggers();

    private final RolUsuarioService rolUsuarioService;

    /**
     * Crea el controlador de consulta de RolUsuario.
     *
     * @param rolUsuarioService servicio de RolUsuario
     */
    public RolUsuarioConsultaController(RolUsuarioService rolUsuarioService) {
        logs.setLogger(RolUsuarioConsultaController.class);
        this.rolUsuarioService = rolUsuarioService;
    }

    /**
     * Lista todos los registros de RolUsuario.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar RolUsuario")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/rol-usuario");
        try {
            ResponseModel response = Util.generateOKResponse(rolUsuarioService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de RolUsuario por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener RolUsuario por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/rol-usuario/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(rolUsuarioService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
