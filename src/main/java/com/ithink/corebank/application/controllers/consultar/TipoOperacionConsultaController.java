package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.TipoOperacionService;
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
 * Consultas de TipoOperacion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/tipo-operacion")
@Tag(name = "Consultar - TipoOperacion", description = "Consultas de TipoOperacion")
public class TipoOperacionConsultaController {

    private static final Loggers logs = new Loggers();

    private final TipoOperacionService tipoOperacionService;

    /**
     * Crea el controlador de consulta de TipoOperacion.
     *
     * @param tipoOperacionService servicio de TipoOperacion
     */
    public TipoOperacionConsultaController(TipoOperacionService tipoOperacionService) {
        logs.setLogger(TipoOperacionConsultaController.class);
        this.tipoOperacionService = tipoOperacionService;
    }

    /**
     * Lista todos los registros de TipoOperacion.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar TipoOperacion")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/tipo-operacion");
        try {
            ResponseModel response = Util.generateOKResponse(tipoOperacionService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de TipoOperacion por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener TipoOperacion por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/tipo-operacion/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(tipoOperacionService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
