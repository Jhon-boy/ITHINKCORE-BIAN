package com.ithink.corebank.application.controllers.consultar;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Consultas de persona y su detalle demográfico.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/persona")
@Tag(name = "Consultar - Persona", description = "Consultas de personas y detalle (TPERSONA / TPERSONADETALLE)")
public class PersonaConsultaController {

    private static final Loggers logs = new Loggers();

    private final PersonaService personaService;
    private final PersonaDetalleService personaDetalleService;

    /**
     * Crea el controlador de consulta de persona.
     *
     * @param personaService servicio de persona
     * @param personaDetalleService servicio de detalle
     */
    public PersonaConsultaController(
            PersonaService personaService,
            PersonaDetalleService personaDetalleService) {
        logs.setLogger(PersonaConsultaController.class);
        this.personaService = personaService;
        this.personaDetalleService = personaDetalleService;
    }

    /**
     * Lista personas.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar personas")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/persona");
        try {
            List<PersonaDto> data = personaService.findAll();
            ResponseModel response = Util.generateOKResponse(data);
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene una persona por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener persona por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/persona/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(personaService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene el detalle demográfico/laboral de una persona.
     *
     * @param id identificador de persona
     * @return respuesta HTTP
     */
    @GetMapping("/{id}/detalle")
    @Operation(summary = "Obtener detalle de persona (TPERSONADETALLE)")
    public ResponseModel obtenerDetalle(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/persona/" + id + "/detalle");
        try {
            PersonaDetalleDto data = personaDetalleService.findByIdPersona(id);
            ResponseModel response = Util.generateOKResponse(data);
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
