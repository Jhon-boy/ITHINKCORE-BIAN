package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.MedioService;
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
 * Consultas de Medio.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/medio")
@Tag(name = "Consultar - Medio", description = "Consultas de Medio")
public class MedioConsultaController {

    private static final Loggers logs = new Loggers();

    private final MedioService medioService;

    /**
     * Crea el controlador de consulta de Medio.
     *
     * @param medioService servicio de Medio
     */
    public MedioConsultaController(MedioService medioService) {
        logs.setLogger(MedioConsultaController.class);
        this.medioService = medioService;
    }

    /**
     * Lista todos los registros de Medio.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Medio")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/medio");
        try {
            ResponseModel response = Util.generateOKResponse(medioService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Medio por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Medio por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/medio/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(medioService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
