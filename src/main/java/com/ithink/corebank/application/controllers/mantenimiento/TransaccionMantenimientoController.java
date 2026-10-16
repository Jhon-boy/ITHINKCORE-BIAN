package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.TransaccionDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.TransaccionService;
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
 * Mantenimiento de Transaccion.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/transaccion")
@Tag(name = "Mantenimiento - Transaccion", description = "Altas de Transaccion")
public class TransaccionMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final TransaccionService transaccionService;

    /**
     * Crea el controlador de mantenimiento de Transaccion.
     *
     * @param transaccionService servicio de Transaccion
     */
    public TransaccionMantenimientoController(TransaccionService transaccionService) {
        logs.setLogger(TransaccionMantenimientoController.class);
        this.transaccionService = transaccionService;
    }

    /**
     * Registra un Transaccion.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Transaccion")
    public ResponseModel registrar(@RequestBody TransaccionDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/transaccion/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(transaccionService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
