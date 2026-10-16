package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.BancoDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.BancoService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mantenimiento de Banco.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/banco")
@Tag(name = "Mantenimiento - Banco", description = "Altas de Banco")
public class BancoMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final BancoService bancoService;

    /**
     * Crea el controlador de mantenimiento de Banco.
     *
     * @param bancoService servicio de Banco
     */
    public BancoMantenimientoController(BancoService bancoService) {
        logs.setLogger(BancoMantenimientoController.class);
        this.bancoService = bancoService;
    }

    /**
     * Registra un Banco.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Banco")
    public ResponseModel registrar(@RequestBody BancoDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/banco/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(bancoService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
