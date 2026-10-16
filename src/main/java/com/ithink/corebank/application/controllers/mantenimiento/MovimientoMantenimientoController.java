package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.MovimientoDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.MovimientoService;
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
 * Mantenimiento de Movimiento.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/movimiento")
@Tag(name = "Mantenimiento - Movimiento", description = "Altas de Movimiento")
public class MovimientoMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final MovimientoService movimientoService;

    /**
     * Crea el controlador de mantenimiento de Movimiento.
     *
     * @param movimientoService servicio de Movimiento
     */
    public MovimientoMantenimientoController(MovimientoService movimientoService) {
        logs.setLogger(MovimientoMantenimientoController.class);
        this.movimientoService = movimientoService;
    }

    /**
     * Registra un Movimiento.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Movimiento")
    public ResponseModel registrar(@RequestBody MovimientoDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/movimiento/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(movimientoService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
