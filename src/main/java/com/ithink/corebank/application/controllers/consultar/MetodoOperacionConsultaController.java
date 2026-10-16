package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.MetodoOperacionService;
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
 * Consultas de MetodoOperacion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/metodo-operacion")
@Tag(name = "Consultar - MetodoOperacion", description = "Consultas de MetodoOperacion")
public class MetodoOperacionConsultaController {

    private static final Loggers logs = new Loggers();

    private final MetodoOperacionService metodoOperacionService;

    /**
     * Crea el controlador de consulta de MetodoOperacion.
     *
     * @param metodoOperacionService servicio de MetodoOperacion
     */
    public MetodoOperacionConsultaController(MetodoOperacionService metodoOperacionService) {
        logs.setLogger(MetodoOperacionConsultaController.class);
        this.metodoOperacionService = metodoOperacionService;
    }

    /**
     * Lista todos los registros de MetodoOperacion.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar MetodoOperacion")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/metodo-operacion");
        try {
            ResponseModel response = Util.generateOKResponse(metodoOperacionService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de MetodoOperacion por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener MetodoOperacion por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/metodo-operacion/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(metodoOperacionService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
