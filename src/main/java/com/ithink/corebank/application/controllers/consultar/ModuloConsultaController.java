package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.ModuloService;
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
 * Consultas de Modulo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/modulo")
@Tag(name = "Consultar - Modulo", description = "Consultas de Modulo")
public class ModuloConsultaController {

    private static final Loggers logs = new Loggers();

    private final ModuloService moduloService;

    /**
     * Crea el controlador de consulta de Modulo.
     *
     * @param moduloService servicio de Modulo
     */
    public ModuloConsultaController(ModuloService moduloService) {
        logs.setLogger(ModuloConsultaController.class);
        this.moduloService = moduloService;
    }

    /**
     * Lista todos los registros de Modulo.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Modulo")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/modulo");
        try {
            ResponseModel response = Util.generateOKResponse(moduloService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Modulo por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Modulo por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/modulo/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(moduloService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
