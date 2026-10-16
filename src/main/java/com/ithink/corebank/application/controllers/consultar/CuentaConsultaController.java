package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.CuentaService;
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
 * Consultas de cuenta.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/cuenta")
@Tag(name = "Consultar - Cuenta", description = "Consultas de cuentas")
public class CuentaConsultaController {

    private static final Loggers logs = new Loggers();

    private final CuentaService cuentaService;

    /**
     * Crea el controlador de consulta de cuenta.
     *
     * @param cuentaService servicio de cuenta
     */
    public CuentaConsultaController(CuentaService cuentaService) {
        logs.setLogger(CuentaConsultaController.class);
        this.cuentaService = cuentaService;
    }

    /**
     * Lista cuentas.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar cuentas")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/cuenta");
        try {
            ResponseModel response = Util.generateOKResponse(cuentaService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene una cuenta por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/cuenta/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(cuentaService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
