package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.EstadoMovimientoService;
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
 * Consultas de EstadoMovimiento.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/estado-movimiento")
@Tag(name = "Consultar - EstadoMovimiento", description = "Consultas de EstadoMovimiento")
public class EstadoMovimientoConsultaController {

    private static final Loggers logs = new Loggers();

    private final EstadoMovimientoService estadoMovimientoService;

    /**
     * Crea el controlador de consulta de EstadoMovimiento.
     *
     * @param estadoMovimientoService servicio de EstadoMovimiento
     */
    public EstadoMovimientoConsultaController(EstadoMovimientoService estadoMovimientoService) {
        logs.setLogger(EstadoMovimientoConsultaController.class);
        this.estadoMovimientoService = estadoMovimientoService;
    }

    /**
     * Lista todos los registros de EstadoMovimiento.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar EstadoMovimiento")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/estado-movimiento");
        try {
            ResponseModel response = Util.generateOKResponse(estadoMovimientoService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de EstadoMovimiento por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener EstadoMovimiento por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/estado-movimiento/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(estadoMovimientoService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
