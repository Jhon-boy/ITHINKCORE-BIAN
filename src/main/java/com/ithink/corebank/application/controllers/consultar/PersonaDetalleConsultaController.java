package com.ithink.corebank.application.controllers.consultar;

import com.ithink.corebank.application.dto.ResponseModel;
import com.ithink.corebank.application.services.PersonaDetalleService;
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
 * Consultas de PersonaDetalle.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
@RequestMapping("/api/consultar/persona-detalle")
@Tag(name = "Consultar - PersonaDetalle", description = "Consultas de PersonaDetalle")
public class PersonaDetalleConsultaController {

    private static final Loggers logs = new Loggers();

    private final PersonaDetalleService personaDetalleService;

    /**
     * Crea el controlador de consulta de PersonaDetalle.
     *
     * @param personaDetalleService servicio de PersonaDetalle
     */
    public PersonaDetalleConsultaController(PersonaDetalleService personaDetalleService) {
        logs.setLogger(PersonaDetalleConsultaController.class);
        this.personaDetalleService = personaDetalleService;
    }

    /**
     * Lista todos los registros de PersonaDetalle.
     *
     * @return respuesta HTTP
     */
    @GetMapping
    @Operation(summary = "Listar PersonaDetalle")
    public ResponseModel listar() {
        logs.info(Const.ML_INI + "GET /api/consultar/persona-detalle");
        try {
            ResponseModel response = Util.generateOKResponse(personaDetalleService.findAll());
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }

    /**
     * Obtiene un registro de PersonaDetalle por id.
     *
     * @param id identificador
     * @return respuesta HTTP
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener PersonaDetalle por id")
    public ResponseModel obtener(@PathVariable Integer id) {
        logs.info(Const.ML_INI + "GET /api/consultar/persona-detalle/" + id);
        try {
            ResponseModel response = Util.generateOKResponse(personaDetalleService.findById(id));
            logs.info(Const.ML_FIN + Util.toJson(response));
            return response;
        } catch (Exception ex) {
            logs.error(Const.ML_ERROR + ex.getMessage(), ex);
            return Util.generateBadResponseDefault();
        }
    }
}
