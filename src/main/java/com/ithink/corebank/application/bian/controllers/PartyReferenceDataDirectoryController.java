package com.ithink.corebank.application.bian.controllers;

import com.ithink.bian.api.PartyReferenceDataDirectoryApi;
import com.ithink.bian.model.PartyDemographics;
import com.ithink.bian.model.PartyControl;
import com.ithink.bian.model.PartyReferenceDataDirectoryEntry;
import com.ithink.corebank.application.bian.services.DemographicsBianService;
import com.ithink.corebank.application.bian.services.PartyReferenceDataDirectoryService;
import com.ithink.corebank.security.loggers.Loggers;
import com.ithink.corebank.util.Const;
import com.ithink.corebank.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador BIAN del Service Domain Party Reference Data Directory.
 * Implementa el slice CR: Register, Retrieve y Update.
 * Los endpoints internos {@code /api/*} no se modifican.
 *
 * @author Jhon
 * @version 1.0
 */
@RestController
public class PartyReferenceDataDirectoryController implements PartyReferenceDataDirectoryApi {

    private static final Loggers logs = new Loggers();

    private final PartyReferenceDataDirectoryService partyReferenceDataDirectoryService;
    private final DemographicsBianService demographicsBianService;

    /**
     * Crea el controlador BIAN de Party Reference Data Directory.
     *
     * @param partyReferenceDataDirectoryService servicio del directorio
     * @param demographicsBianService servicio de demografía
     */
    public PartyReferenceDataDirectoryController(
            PartyReferenceDataDirectoryService partyReferenceDataDirectoryService,
            DemographicsBianService demographicsBianService) {
        logs.setLogger(PartyReferenceDataDirectoryController.class);
        this.partyReferenceDataDirectoryService = partyReferenceDataDirectoryService;
        this.demographicsBianService = demographicsBianService;
    }

    /**
     * InCR Register: registra una persona en el directorio.
     *
     * @param partyReferenceDataDirectoryEntry payload BIAN
     * @return entrada registrada
     */
    @Override
    public ResponseEntity<PartyReferenceDataDirectoryEntry> register(PartyReferenceDataDirectoryEntry partyReferenceDataDirectoryEntry) {
        logs.info(Const.ML_INI + "POST /PartyReferenceDataDirectory/Register" + Util.toJson(partyReferenceDataDirectoryEntry));
        PartyReferenceDataDirectoryEntry response = partyReferenceDataDirectoryService.register(partyReferenceDataDirectoryEntry);
        logs.info(Const.ML_FIN + "POST /PartyReferenceDataDirectory/Register" + Util.toJson(response));
        return ResponseEntity.ok(response);
    }

    /**
     * ReCR Retrieve: consulta una entrada del directorio.
     *
     * @param partyreferencedatadirectoryid identificador (idPersona)
     * @return entrada encontrada
     */
    @Override
    public ResponseEntity<PartyReferenceDataDirectoryEntry> retrieve(String partyreferencedatadirectoryid) {
        logs.info(Const.ML_INI + "GET /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Retrieve");
        PartyReferenceDataDirectoryEntry response = partyReferenceDataDirectoryService.retrieve(partyreferencedatadirectoryid);
        logs.info(Const.ML_FIN + "GET /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Retrieve");
        return ResponseEntity.ok(response);
    }

    /**
     * UpCR Update: actualiza una entrada del directorio.
     *
     * @param partyreferencedatadirectoryid identificador (idPersona)
     * @param partyReferenceDataDirectoryEntry payload BIAN
     * @return entrada actualizada
     */
    @Override
    public ResponseEntity<PartyReferenceDataDirectoryEntry> update(String partyreferencedatadirectoryid,
            PartyReferenceDataDirectoryEntry partyReferenceDataDirectoryEntry) {
        logs.info(Const.ML_INI + "PUT /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Update");
        PartyReferenceDataDirectoryEntry response =
                partyReferenceDataDirectoryService.update(
                        partyreferencedatadirectoryid, partyReferenceDataDirectoryEntry);
        logs.info(Const.ML_FIN + "PUT /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Update");
        return ResponseEntity.ok(response);
    }

    /**
     * Activa o desactiva al cliente.
     *
     * @param partyreferencedatadirectoryid identificador de la persona
     * @param partyControl payload de vigencia
     * @return vigencia resultante
     */
    @Override
    public ResponseEntity<PartyControl> control(String partyreferencedatadirectoryid, PartyControl partyControl) {
        logs.info(Const.ML_INI + "PUT /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Control");
        PartyControl response = partyReferenceDataDirectoryService.control(partyreferencedatadirectoryid, partyControl);
        logs.info(Const.ML_FIN + "PUT /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Control");
        return ResponseEntity.ok(response);
    }

    /**
     * Registra la demografía del cliente.
     *
     * @param partyreferencedatadirectoryid identificador de la persona
     * @param demographics payload BIAN
     * @return demografía persistida
     */
    @Override
    public ResponseEntity<PartyDemographics> registerDemographics(
            String partyreferencedatadirectoryid, PartyDemographics demographics) {
        logs.info(Const.ML_INI + "POST /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Demographics/Register");
        PartyDemographics response = demographicsBianService.register(partyreferencedatadirectoryid, demographics);
        logs.info(Const.ML_FIN + "POST /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Demographics/Register");
        return ResponseEntity.ok(response);
    }

    /**
     * Consulta la demografía del cliente.
     *
     * @param partyreferencedatadirectoryid identificador de la persona
     * @return demografía
     */
    @Override
    public ResponseEntity<PartyDemographics> retrieveDemographics(String partyreferencedatadirectoryid) {
        logs.info(Const.ML_INI + "GET /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Demographics/Retrieve");
        PartyDemographics response = demographicsBianService.retrieve(partyreferencedatadirectoryid);
        logs.info(Const.ML_FIN + "GET /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Demographics/Retrieve");
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza la demografía del cliente.
     *
     * @param partyreferencedatadirectoryid identificador de la persona
     * @param demographics payload BIAN
     * @return demografía actualizada
     */
    @Override
    public ResponseEntity<PartyDemographics> updateDemographics(
            String partyreferencedatadirectoryid, PartyDemographics demographics) {
        logs.info(Const.ML_INI + "PUT /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Demographics/Update");
        PartyDemographics response = demographicsBianService.update(partyreferencedatadirectoryid, demographics);
        logs.info(Const.ML_FIN + "PUT /PartyReferenceDataDirectory/" + partyreferencedatadirectoryid + "/Demographics/Update");
        return ResponseEntity.ok(response);
    }
}
