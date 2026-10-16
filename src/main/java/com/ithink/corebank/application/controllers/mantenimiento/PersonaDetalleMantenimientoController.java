package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.PersonaDetalleDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.PersonaDetalleService;
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
 * Mantenimiento de PersonaDetalle.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/persona-detalle")
@Tag(name = "Mantenimiento - PersonaDetalle", description = "Altas de PersonaDetalle")
public class PersonaDetalleMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final PersonaDetalleService personaDetalleService;

    /**
     * Crea el controlador de mantenimiento de PersonaDetalle.
     *
     * @param personaDetalleService servicio de PersonaDetalle
     */
    public PersonaDetalleMantenimientoController(PersonaDetalleService personaDetalleService) {
        logs.setLogger(PersonaDetalleMantenimientoController.class);
        this.personaDetalleService = personaDetalleService;
    }

    /**
     * Registra un PersonaDetalle.
     *
     * @param dto datos a registrar
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar PersonaDetalle")
    public ResponseModel registrar(@RequestBody PersonaDetalleDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/persona-detalle/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(personaDetalleService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
