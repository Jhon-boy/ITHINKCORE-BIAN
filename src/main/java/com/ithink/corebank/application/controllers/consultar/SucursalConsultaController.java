package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.SucursalService;
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
 * Consultas de Sucursal.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/sucursal")
@Tag(name = "Consultar - Sucursal", description = "Consultas de Sucursal")
public class SucursalConsultaController {

    private static final Loggers logs = new Loggers();

    private final SucursalService sucursalService;

    /**
     * Crea el controlador de consulta de Sucursal.
     *
     * @param sucursalService servicio de Sucursal
     */
    public SucursalConsultaController(SucursalService sucursalService) {
        logs.setLogger(SucursalConsultaController.class);
        this.sucursalService = sucursalService;
    }

    /**
     * Lista todos los registros de Sucursal.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Sucursal")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/sucursal");
        try {
            ResponseModel response = Util.generateOKResponse(sucursalService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Sucursal por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Sucursal por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/sucursal/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(sucursalService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
