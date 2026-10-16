package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.UsuarioService;
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
 * Consultas de Usuario.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/usuario")
@Tag(name = "Consultar - Usuario", description = "Consultas de Usuario")
public class UsuarioConsultaController {

    private static final Loggers logs = new Loggers();

    private final UsuarioService usuarioService;

    /**
     * Crea el controlador de consulta de Usuario.
     *
     * @param usuarioService servicio de Usuario
     */
    public UsuarioConsultaController(UsuarioService usuarioService) {
        logs.setLogger(UsuarioConsultaController.class);
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos los registros de Usuario.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Usuario")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/usuario");
        try {
            ResponseModel response = Util.generateOKResponse(usuarioService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Usuario por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Usuario por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/usuario/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(usuarioService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
