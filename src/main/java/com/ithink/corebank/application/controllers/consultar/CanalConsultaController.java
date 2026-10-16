package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.CanalService;
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
 * Consultas de Canal.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/canal")
@Tag(name = "Consultar - Canal", description = "Consultas de Canal")
public class CanalConsultaController {

    private static final Loggers logs = new Loggers();

    private final CanalService canalService;

    /**
     * Crea el controlador de consulta de Canal.
     *
     * @param canalService servicio de Canal
     */
    public CanalConsultaController(CanalService canalService) {
        logs.setLogger(CanalConsultaController.class);
        this.canalService = canalService;
    }

    /**
     * Lista todos los registros de Canal.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Canal")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/canal");
        try {
            ResponseModel response = Util.generateOKResponse(canalService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Canal por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Canal por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/canal/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(canalService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
