package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.BancoService;
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
 * Consultas de Banco.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/banco")
@Tag(name = "Consultar - Banco", description = "Consultas de Banco")
public class BancoConsultaController {

    private static final Loggers logs = new Loggers();

    private final BancoService bancoService;

    /**
     * Crea el controlador de consulta de Banco.
     *
     * @param bancoService servicio de Banco
     */
    public BancoConsultaController(BancoService bancoService) {
        logs.setLogger(BancoConsultaController.class);
        this.bancoService = bancoService;
    }

    /**
     * Lista todos los registros de Banco.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar Banco")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/banco");
        try {
            ResponseModel response = Util.generateOKResponse(bancoService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de Banco por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener Banco por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/banco/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(bancoService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
