package com.ithink.corebank.application.controllers.mantenimiento;

import com.ithink.corebank.application.dto.PersonaDetalleDto;
import com.ithink.corebank.application.dto.PersonaDto;
import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.PersonaDetalleService;
import com.ithink.corebank.application.services.PersonaService;
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
 * Mantenimiento de personas: registrar y completar detalle.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/mantenimiento/persona")
@Tag(name = "Mantenimiento - Persona",
        description = "Registrar persona y completar detalle demográfico/laboral")
public class PersonaMantenimientoController {

    private static final Loggers logs = new Loggers();

    private final PersonaService personaService;
    private final PersonaDetalleService personaDetalleService;

    /**
     * Crea el controlador de mantenimiento de persona.
     *
     * @param personaService servicio de persona
     * @param personaDetalleService servicio de detalle
     */
    public PersonaMantenimientoController(
            PersonaService personaService,
            PersonaDetalleService personaDetalleService) {
        logs.setLogger(PersonaMantenimientoController.class);
        this.personaService = personaService;
        this.personaDetalleService = personaDetalleService;
    }

    /**
     * Registra una persona.
     *
     * @param dto datos de la persona
     * @return respuesta HTTP
     */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar persona", description = "Alta en TPERSONA")
    public ResponseModel registrar(@RequestBody PersonaDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/persona/registrar");
        try {
            ResponseModel response = Util.generateOKResponse(personaService.save(dto));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Completa o actualiza el detalle de una persona.
     *
     * @param dto datos de TPERSONADETALLE (requiere idPersona)
     * @return respuesta HTTP
     */
    @PostMapping("/completar")
    @Operation(summary = "Completar persona",
            description = "Alta/actualización de TPERSONADETALLE (estado civil, ocupación, etc.)")
    public ResponseModel completar(@RequestBody PersonaDetalleDto dto) {
        logs.info(Const.ML_INI + "POST /api/mantenimiento/persona/completar");
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
