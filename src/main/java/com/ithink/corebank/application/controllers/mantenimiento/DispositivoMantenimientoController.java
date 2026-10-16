package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.DispositivoDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.DispositivoService;
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
 * Mantenimiento de Dispositivo.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/dispositivo")
@Tag(name = "Mantenimiento - Dispositivo", description = "Altas de Dispositivo")
public class DispositivoMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final DispositivoService dispositivoService;

    /**
     * Crea el controlador de mantenimiento de Dispositivo.
     *
     * @param dispositivoService servicio de Dispositivo
     */
    public DispositivoMantenimientoController(DispositivoService dispositivoService) {
        logs.setLogger(DispositivoMantenimientoController.class);
        this.dispositivoService = dispositivoService;
    }

    /**
     * Registra un Dispositivo.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Dispositivo")
    public ResponseModel registrar(@RequestBody DispositivoDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/dispositivo/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(dispositivoService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
