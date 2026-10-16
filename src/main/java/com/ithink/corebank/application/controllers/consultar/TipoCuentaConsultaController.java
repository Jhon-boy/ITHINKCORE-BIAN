package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.TipoCuentaService;
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
 * Consultas de TipoCuenta.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/tipo-cuenta")
@Tag(name = "Consultar - TipoCuenta", description = "Consultas de TipoCuenta")
public class TipoCuentaConsultaController {

    private static final Loggers logs = new Loggers();

    private final TipoCuentaService tipoCuentaService;

    /**
     * Crea el controlador de consulta de TipoCuenta.
     *
     * @param tipoCuentaService servicio de TipoCuenta
     */
    public TipoCuentaConsultaController(TipoCuentaService tipoCuentaService) {
        logs.setLogger(TipoCuentaConsultaController.class);
        this.tipoCuentaService = tipoCuentaService;
    }

    /**
     * Lista todos los registros de TipoCuenta.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar TipoCuenta")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/tipo-cuenta");
        try {
            ResponseModel response = Util.generateOKResponse(tipoCuentaService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de TipoCuenta por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener TipoCuenta por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/tipo-cuenta/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(tipoCuentaService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
