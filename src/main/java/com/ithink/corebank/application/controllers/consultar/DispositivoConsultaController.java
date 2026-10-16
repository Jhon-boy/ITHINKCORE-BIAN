package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.DispositivoService;
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
 * Consultas de Dispositivo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/dispositivo")
@Tag(name = "Consultar - Dispositivo", description = "Consultas de Dispositivo")
public class DispositivoConsultaController {

    private static final Loggers logs = new Loggers();

    private final DispositivoService dispositivoService;

    /**
     * Crea el controlador de consulta de Dispositivo.
     *
     * @param dispositivoService servicio de Dispositivo
     */
    public DispositivoConsultaController(DispositivoService dispositivoService) {
        logs.setLogger(DispositivoConsultaController.class);
        this.dispositivoService = dispositivoService;
    }

    /**
     * Lista todos los registros de Dispositivo.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Dispositivo")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/dispositivo");
        try {
            ResponseModel response = Util.generateOKResponse(dispositivoService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Dispositivo por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Dispositivo por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/dispositivo/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(dispositivoService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
