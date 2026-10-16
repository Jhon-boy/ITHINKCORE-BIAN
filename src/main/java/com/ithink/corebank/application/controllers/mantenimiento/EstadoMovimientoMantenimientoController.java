package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.EstadoMovimientoDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.EstadoMovimientoService;
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
 * Mantenimiento de EstadoMovimiento.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/estado-movimiento")
@Tag(name = "Mantenimiento - EstadoMovimiento", description = "Altas de EstadoMovimiento")
public class EstadoMovimientoMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final EstadoMovimientoService estadoMovimientoService;

    /**
     * Crea el controlador de mantenimiento de EstadoMovimiento.
     *
     * @param estadoMovimientoService servicio de EstadoMovimiento
     */
    public EstadoMovimientoMantenimientoController(EstadoMovimientoService estadoMovimientoService) {
        logs.setLogger(EstadoMovimientoMantenimientoController.class);
        this.estadoMovimientoService = estadoMovimientoService;
    }

    /**
     * Registra un EstadoMovimiento.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar EstadoMovimiento")
    public ResponseModel registrar(@RequestBody EstadoMovimientoDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/estado-movimiento/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(estadoMovimientoService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
