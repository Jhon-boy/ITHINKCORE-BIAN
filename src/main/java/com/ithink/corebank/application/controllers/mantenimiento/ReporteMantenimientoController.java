package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.ReporteDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.ReporteService;
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
 * Mantenimiento de Reporte.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/reporte")
@Tag(name = "Mantenimiento - Reporte", description = "Altas de Reporte")
public class ReporteMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final ReporteService reporteService;

    /**
     * Crea el controlador de mantenimiento de Reporte.
     *
     * @param reporteService servicio de Reporte
     */
    public ReporteMantenimientoController(ReporteService reporteService) {
        logs.setLogger(ReporteMantenimientoController.class);
        this.reporteService = reporteService;
    }

    /**
     * Registra un Reporte.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar Reporte")
    public ResponseModel registrar(@RequestBody ReporteDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/reporte/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(reporteService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
