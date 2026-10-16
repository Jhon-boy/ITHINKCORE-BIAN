package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.RolService;
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
 * Consultas de Rol.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/rol")
@Tag(name = "Consultar - Rol", description = "Consultas de Rol")
public class RolConsultaController {

    private static final Loggers logs = new Loggers();

    private final RolService rolService;

    /**
     * Crea el controlador de consulta de Rol.
     *
     * @param rolService servicio de Rol
     */
    public RolConsultaController(RolService rolService) {
        logs.setLogger(RolConsultaController.class);
        this.rolService = rolService;
    }

    /**
     * Lista todos los registros de Rol.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Rol")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/rol");
        try {
            ResponseModel response = Util.generateOKResponse(rolService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Rol por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Rol por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/rol/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(rolService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
